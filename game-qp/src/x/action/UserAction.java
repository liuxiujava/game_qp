package x.action;

import com.alibaba.fastjson.JSON;
import jsa.cryptograph.CryptographFactory;
import jsa.event.EventContainer;
import jsa.id.JsaIdGenerator;
import jsa.ioc.annotation.Controller;
import jsa.ioc.annotation.RequestMapping;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import jsa.orm.IDataSource;
import jsa.orm.JsaOrmFactory;
import jsa.utils.RandomUtil;
import ws.nett.message.IMessage;
import ws.nett.session.IoSession;
import x.context.NoticeContext;
import x.context.RoomContext;
import x.context.UserContext;
import x.db.SqlManager;
import x.executor.ExecutorDispatcher;
import x.frame.event.RoomEvent;
import x.game.RoomInfo;
import x.msg.RequestData;
import x.msg.ResponseData;
import x.msg.imp.*;
import x.msg.request.*;
import x.msg.response.*;
import x.utils.*;
import x.vo.*;

import java.util.Date;
import java.util.List;

@Controller
public class UserAction {
    final static Logger logger = LoggerFactory.getLogger(UserAction.class, ExecutorDispatcher.LogActor);


    @RequestMapping("" + CmdType.CMD1010)
    public void CMD1010(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        WxLandMessage landMessage = JsonUtils.jsonToPojo(requestData.getParams(), WxLandMessage.class);
        String url = null;
        url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + ServerInitConfig.getWxappid() +
                "&secret=" + ServerInitConfig.getWxsecret() +
                "&code=" + landMessage.getCode() + "&grant_type=authorization_code";

        String resp = null;
        try {
            resp = Http.get(url);
            logger.error(url + " : " + resp); //TODO rm
        } catch (Exception e) {
            logger.error("360 dawanjia error", e);
        }
        ResponseData responseData = new ResponseData(requestData.getAction());

        logger.error(resp);
        com.alibaba.fastjson.JSONObject jo = JSON.parseObject(resp);
        if (jo.get("errcode") != null) {
            responseData.setCode(1);  //账户不存在
            responseData.setMsg("" + jo.get("errcode"));
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }

        String accessToken = (String) jo.get("access_token");
        String openid = (String) jo.get("openid");
        String unionid = (String) jo.get("unionid");
        if (StringUtil.isEmpty(accessToken) || StringUtil.isEmpty(openid) || StringUtil.isEmpty(unionid)) {
            responseData.setCode(2);  //账户不存在
            responseData.setMsg("返回数据异常");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }

        UserVo userVo = null;
        String ptId = UserContext.getPtIdByAccount(openid);
        boolean isReLogin = true;
        if (ptId != null) {
            userVo = UserContext.getUser(ptId);
        }
        if (userVo != null) {
            userVo = UserContext.checkLand(session, userVo.getToken());
        } else {
            userVo = new UserVo();
            userVo.setAccount(openid);
            IDataSource ds = JsaOrmFactory.getDataSource();
            userVo = ds.getOneByValue(userVo, "account");
            isReLogin = false;
        }

        if (userVo == null) {  //获取用户信息 并且注册
            // TODO：根据Openid或Unionid对数据库进行查询，如果查询到对应的用户数据，则不需要再向微信服务器发送请求去返回数据。
            // TODO: 建议使用Unionid作为查询条件。
            com.alibaba.fastjson.JSONObject userInfo = getUserInfoByAccessToken(accessToken); // 新用户
            if (userInfo == null) {
                responseData.setCode(3);  //账户不存在
                responseData.setMsg("获取用户信息错误");
                cmd.setData(JsonUtils.objectToJson(responseData));
                session.write(cmd);
                return;
            }
            // 数据库插入的操作
            IDataSource ds = JsaOrmFactory.getDataSource();
            userVo = new UserVo();
            userVo.setAccount(openid);
            userVo.setPassWd(openid);
            userVo.setUserName((String) userInfo.get("nickname"));
            userVo.setCreateTime(new Date());
            int sex = (int) userInfo.get("sex");
            userVo.setSex(sex == 1 ? 2 : 1);
            userVo.setHeadId((String) userInfo.get("headimgurl"));
            userVo.setLoginTime(new Date());
            ds.save(userVo);

            userVo.setPtId(UserContext.StartId + userVo.getId());
            SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "ptId");
        }

        RoomInfo roomInfo = null;
        if (!isReLogin) {
            userVo.setToken(JsaIdGenerator.getId(1) + "");
            UserContext.addUser(session, userVo);
            if (userVo.getRoomId() != 0) {
                roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());
                if (roomInfo == null) {
                    userVo.setRoomId(0);
                    SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "roomId");
                }
            }
        }
        session.setAttribute(KeyUtil.PtId, userVo.getPtId());

        userVo.setLoginTime(new Date());
        userVo.setOnline(true);
        SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "loginTime");

        cmd.setCommand(CmdType.CMD101 + "");
        responseData.setAction(CmdType.CMD101);
        responseData.setCode(0);  //成功
        responseData.setMsg("登录成功" + userVo.getRoomId());
        Response101 landResponse = new Response101();
        landResponse.setId(Integer.parseInt(userVo.getPtId()));
        landResponse.setDiamond(userVo.getDiamond());
        landResponse.setRoomId(userVo.getRoomId() + "");
        landResponse.setUserName(userVo.getUserName());
        landResponse.setToken(userVo.getToken());
        landResponse.setHead(userVo.getHeadId());
        responseData.setParams(landResponse);

        cmd.setData(responseData);
        session.write(cmd);

        if (roomInfo != null) {
            RoomContext.upLogin(roomInfo, userVo);
        }

    }

    /**
     * 根据accessToken获取用户个人公开信息
     *
     * @param accessToken
     * @return
     */
    private com.alibaba.fastjson.JSONObject getUserInfoByAccessToken(String accessToken) {
        if (StringUtil.isEmpty(accessToken)) {
            return null;  //"accessToken为空";
        }
        String get_userInfo_url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + accessToken + "&openid=" + ServerInitConfig.getWxappid();

        String userInfo_result = null;
        try {
            userInfo_result = Http.get(get_userInfo_url);
            userInfo_result.getBytes("UTF-8");

            logger.error(get_userInfo_url + " : " + userInfo_result);
        } catch (Exception e) {
            logger.error("360 dawanjia error", e);
        }


        if (!userInfo_result.equals("errcode")) {
            return JSON.parseObject(userInfo_result);
        }
        return null;  //"获取用户信息失败"
    }


    @RequestMapping("" + CmdType.CMD101)
    public void cmd101(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        Request101 landMessage = JsonUtils.jsonToPojo(requestData.getParams(), Request101.class);

        boolean isReLogin = true;
        ResponseData responseData = new ResponseData(requestData.getAction());
        UserVo userVo = null;
        RoomInfo roomInfo = null;
        if (landMessage != null) {
            String ptId = UserContext.getPtIdByAccount(landMessage.getPhone());
            if (ptId != null) {
                userVo = UserContext.getUser(ptId);
            }
            if (userVo != null) {
                userVo = UserContext.checkLand(session, userVo.getToken());
            } else {
                userVo = new UserVo();
                userVo.setPhone(landMessage.getPhone());
                IDataSource ds = JsaOrmFactory.getDataSource();
                userVo = ds.getOneByValue(userVo, "phone");
                isReLogin = false;
                if (userVo == null) {   //重复
                    responseData.setCode(1);  //账户不存在
                    responseData.setMsg("帐号不存在");
                    cmd.setData(JsonUtils.objectToJson(responseData));
                    session.write(cmd);
                    userVo = new UserVo();
                    userVo.setAccount(landMessage.getPhone());
                    userVo.setPassWd(landMessage.getPwd());
                    userVo.setUserName(landMessage.getPhone());
                    userVo.setCreateTime(new Date());
                    userVo.setSex(1);
                    userVo.setHeadId("1");
                    int head = 5;
                    if (userVo.getSex() == 1) {
                        head = 1;
                    }
                    userVo.setHeadId((RandomUtil.getNexInt(4) + head) + "");
                    userVo.setLoginTime(new Date());
                    ds.save(userVo);
                    userVo.setPtId(UserContext.StartId + userVo.getId());
                    SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "ptId");

                }
            }
            String md5 = CryptographFactory.md5(landMessage.getPwd().getBytes("UTF-8")).toLowerCase();
//                if (md5.equals(userVo.getPassWd())) {
            if (landMessage.getPwd().equals(userVo.getPassWd())) {
                session.setAttribute(KeyUtil.PtId, userVo.getPtId());
            } else {
                responseData.setCode(1);   //帐号密码错误
                responseData.setMsg("帐号密码错误");
                cmd.setData(JsonUtils.objectToJson(responseData));
                session.write(cmd);
                return;
            }

            if (!isReLogin) {
                userVo.setToken(JsaIdGenerator.getId(1) + "");
                UserContext.addUser(session, userVo);
                if (userVo.getRoomId() != 0) {
                    roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());
                    if (roomInfo == null) {
                        userVo.setRoomId(0);
                        SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "roomId");
                    }
                }
            }
            session.setAttribute(KeyUtil.PtId, userVo.getPtId());

            userVo.setLoginTime(new Date());
            userVo.setOnline(true);
            SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "loginTime");
            responseData.setCode(0);  //成功
            responseData.setMsg("登录成功" + userVo.getRoomId());
            Response101 landResponse = new Response101();
            landResponse.setId(userVo.getId());
            landResponse.setRoomId(userVo.getRoomId() + "");
            landResponse.setSex(userVo.getSex());
            landResponse.setUserName(userVo.getUserName());
            landResponse.setToken(userVo.getToken());
            landResponse.setHead(userVo.getHeadId());
            landResponse.setDiamond(userVo.getDiamond());
            landResponse.setStatus(userVo.getStatus());
            landResponse.setPhone(userVo.getPhone());
            landResponse.setStatus(userVo.getStatus());
            landResponse.setNotice("123123123");  //todo  公告
            responseData.setParams(landResponse);
        } else {
            responseData.setCode(-1); //未知错误
        }

        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);

        if (roomInfo != null) {
            RoomContext.upLogin(roomInfo, userVo);
        }
    }


    @RequestMapping("" + CmdType.CMD102)
    public void zhuChe(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        Request102 request102 = JsonUtils.jsonToPojo(requestData.getParams(), Request102.class);

        ResponseData responseData = new ResponseData(requestData.getAction());
        if (request102 != null) {

            UserVo userVo = new UserVo();
            userVo.setPhone(request102.getPhone());
            IDataSource ds = JsaOrmFactory.getDataSource();
            userVo = ds.getOneByValue(userVo, "phone");
            if (userVo == null) {
                responseData.setCode(1);
                responseData.setMsg("手机号码不存在");
            } else {

                //验证 code todo

                userVo.setPassWd(request102.getPwd());
                SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "passWd");
                responseData.setTrue();
            }
        }

        cmd.setData(responseData);
        session.write(cmd);
    }

    @RequestMapping("" + CmdType.CMD103)
    public void cmd103(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        ResponseData responseData = new ResponseData(requestData.getAction());

        responseData.setTrue();
        cmd.setData(responseData);
        session.write(cmd);
    }

    @RequestMapping("" + CmdType.CMD104)
    public void cmd104(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }
        Request104 request = JsonUtils.jsonToPojo(requestData.getParams(), Request104.class);
        ResponseData responseData = new ResponseData(requestData.getAction());
        UserVo vo = SqlManager.getUserVoByRoleNameFromDb(request.getUserName());
        if (vo != null) {
            responseData.setCode(1);
            responseData.setMsg("名字重复");
        } else {

            if (userVo.getStatus() != 1) {
                //todo 认证规则
                String oldName = userVo.getUserName();
                userVo.setUserName(request.getUserName());
                userVo.setSfzNum(request.getSfzNum());
                SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "userName", "sfzNum");

                responseData.setTrue();
                UserContext.getNames().remove(oldName);
                UserContext.getNames().put(userVo.getUserName(), userVo.getPtId());

            } else {
                responseData.setCode(1);
                responseData.setMsg("已实名认证");
            }
        }

        cmd.setData(responseData);
        session.write(cmd);
    }

    @RequestMapping("" + CmdType.CMD105)
    public void cmd105(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }
        Request105 request = JsonUtils.jsonToPojo(requestData.getParams(), Request105.class);
        ResponseData responseData = new ResponseData(requestData.getAction());
        UserVo userVo1 = new UserVo();
        userVo1.setPhone(request.getPhone());
        IDataSource ds = JsaOrmFactory.getDataSource();
        userVo1 = ds.getOneByValue(userVo1, "phone");
        if (userVo.getPhone().equals("0")) {
            if (userVo1 != null) {
                responseData.setCode(1);
                responseData.setMsg("手机号码已被占用");
            } else {
                //todo 验证 code

                userVo.setPhone(request.getPhone());
                userVo.setPassWd(request.getPwd());
                SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "phone", "passWd");
                responseData.setCode(0);
                responseData.setMsg("修改成功");
            }
        } else {
            responseData.setCode(2);
            responseData.setMsg("该账号已绑定手机");
        }

        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }

    @RequestMapping("" + CmdType.CMD106)
    public void cmd106(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }

        if (userVo.getFxTime() != 0 && !DateUtil.isSameday(new Date(userVo.getFxTime()), new Date())) {
            userVo.setFxNum(0);
        }

        if (userVo.getFxNum() < 3) {
            userVo.setFxNum(userVo.getFxNum() + 1);
            userVo.setFxTime(System.currentTimeMillis());
            userVo.setDiamond(userVo.getDiamond() + 10);

            SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "diamond", "fxTime", "fxNum");
        } else {
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(2);
            responseData.setMsg("今日分享次数已达上限");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }

        UserContext.responseIntegral(session, userVo);

    }


    @RequestMapping("" + CmdType.CMD107)
    public void cmd107(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        ResponseData responseData = new ResponseData(requestData.getAction());
        Response107 response107 = new Response107();
        for (NoticeVo noticeVo : NoticeContext.getNoticeVos()) {
            Vo107 vo107 = new Vo107();
            vo107.setId(noticeVo.getId());
            vo107.setTitle(noticeVo.getTitle());
            vo107.setDesc(noticeVo.getContext());
            vo107.setTime(noticeVo.getCreateTime().getTime());
            response107.getTitles().add(vo107);
        }
        responseData.setParams(response107);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }

    @RequestMapping("" + CmdType.CMD108)
    public void cmd108(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }
        ResponseData responseData = new ResponseData(requestData.getAction());
        Request108 request = JsonUtils.jsonToPojo(requestData.getParams(), Request108.class);
        List<UserVo> rankInfoVos = SqlManager.getRankInfo(request.getType(), request.getNum(), request.getPage());
        List<NewRankVo> list = SqlManager.getNewRankVos("select a.value1 as num from (select count(*) as value1 from UserVo) a");
        int maxNum = (int) list.get(0).getNum();
        int maxPage = maxNum / request.getNum() + (maxNum % request.getNum() != 0 ? 1 : 0);

        Response108 response108 = new Response108();
        response108.setMaxPage(maxPage);
        response108.setThisPage(request.getPage());
        for (UserVo vo : rankInfoVos) {
            Vo108 vo108 = new Vo108();
            vo108.setHeadId(vo.getHeadId());
            vo108.setPtId(vo.getPtId());
            vo108.setUserName(vo.getUserName());
            vo108.setLevel(vo.getLevel());
            if (request.getType() == 1) {
                vo108.setValue(vo.getDiamond());
            } else {
                vo108.setValue(vo.getWin());
            }
            response108.getRankings().add(vo108);
        }

        responseData.setParams(response108);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }


    @RequestMapping("u_e")
    public void u_e(IoSession session, IMessage cmd) throws Exception {
        UserVo userVo = UserContext.getUser(session);
        if (userVo == null) {
            logger.error("u_e UserExit UserVo==null");
            return;
        }
        RoomInfo roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());
        if (roomInfo != null) {
            RoomEvent event = new RoomEvent(RoomEvent.ExitRoom, session);
            event.setRoomInfo(roomInfo);
            event.setUserVo(userVo);
            EventContainer.dispatchEvent(event);
        }

        userVo.setOnline(false);
    }

    @RequestMapping("" + CmdType.CMD124)
    public void CMD124(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }

        if (userVo.getRoomId() != 0) {
            RoomInfo roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());
            if (roomInfo != null) {
                RoomEvent event = new RoomEvent(RoomEvent.ExitRoom, session);
                event.setRoomInfo(roomInfo);
                event.setUserVo(userVo);
                EventContainer.dispatchEvent(event);
            }
        }

        UserContext.removeUser(userVo.getPtId());
        userVo.setExitTime(new Date());
        SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "exitTime");
        ResponseData responseData = new ResponseData(requestData.getAction());
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }


    @RequestMapping("" + CmdType.CMD125)
    public void CMD125(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            return;
        }
        RoomInfo roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());
        if (roomInfo == null) {
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(1);
            responseData.setMsg("不再房间里不能操作");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }
        Request125 request = JsonUtils.jsonToPojo(requestData.getParams(), Request125.class);

        if (roomInfo.getQhType() != 0) {
            logger.error(roomInfo.getQhType() + "__" + roomInfo.getQhTime());
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(3);
            responseData.setMsg("已经有求和信息");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }

        if (roomInfo.getType() != 0) {
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(4);
            responseData.setMsg("经典房间不能进行求和");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }

        roomInfo.setqhMessge(request.getType(), userVo.getPtId());

        ResponseData responseData = new ResponseData(requestData.getAction());
        Response125 response125 = new Response125();

        response125.setName(roomInfo.getShowName(roomInfo.getUser(userVo.getPtId()), false));
        response125.setType(request.getType());
        response125.setPtId(userVo.getPtId());

        responseData.setParams(response125);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        RoomContext.responseAll(roomInfo, cmd);

        if (roomInfo.qhEnd()) {
            if (roomInfo.getQhType() == 1) {
                roomInfo.setThisNum(roomInfo.getThisNum() - 1);
                roomInfo.start();
            } else {
                String result = roomInfo.checkWin(null);
                if (!result.equals("-1")) {
                    // 1:作废 不纳入统计  2：作废纳入统计   按平局
                    RoomContext.winResponse(roomInfo, result);
                }
            }
        }

    }

    @RequestMapping("" + CmdType.CMD126)
    public void CMD126(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }

        RoomInfo roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());
        if (roomInfo == null) {
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(1);
            responseData.setMsg("不再房间里不能操作");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }
        Request126 request = JsonUtils.jsonToPojo(requestData.getParams(), Request126.class);
        if (roomInfo.getQhType() == 0) {
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(2);
            responseData.setMsg("没有人发起投票");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }

        roomInfo.updateQhMessage(request.getValue(), userVo.getPtId());

        ResponseData responseData = new ResponseData(requestData.getAction());
        Response126 response = new Response126();
        response.setName(userVo.getUserName());
        response.setPtId(userVo.getPtId());
        response.setValue(request.getValue());

        responseData.setParams(response);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        RoomContext.responseAll(roomInfo, cmd);


        if (roomInfo.qhEnd()) {
            if (roomInfo.getQhType() == 1) {
                roomInfo.setThisNum(roomInfo.getThisNum() - 1);
                roomInfo.start();
            } else {
                String result = roomInfo.checkWin(null);
                if (!result.equals("-1")) {
                    // 1:作废 不纳入统计  2：作废纳入统计   按平局
                    RoomContext.winResponse(roomInfo, result);
                }
            }
        }

    }


}
