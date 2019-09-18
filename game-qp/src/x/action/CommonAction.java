package x.action;

import jsa.ioc.annotation.Controller;
import jsa.ioc.annotation.RequestMapping;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import ws.nett.message.IMessage;
import ws.nett.session.IoSession;
import x.context.NoticeContext;
import x.context.RoomContext;
import x.context.UserContext;
import x.db.SqlManager;
import x.executor.ExecutorDispatcher;
import x.game.RoomInfo;
import x.msg.RequestData;
import x.msg.ResponseData;
import x.msg.imp.*;
import x.msg.request.Request200;
import x.msg.request.Request201;
import x.msg.request.Request202;
import x.msg.response.Response106;
import x.msg.response.Response200;
import x.msg.response.Response201;
import x.utils.CmdType;
import x.utils.JsonUtils;
import x.vo.*;
import x.vo.util.ParsingVoUtil;

import java.util.List;


@Controller
public class CommonAction {

    final static Logger logger = LoggerFactory.getLogger(CommonAction.class, ExecutorDispatcher.LogActor);

    @RequestMapping("" + CmdType.CMD100)
    public void cmd100(IoSession session, IMessage cmd) throws Exception {
        ResponseData data = new ResponseData(Integer.parseInt(cmd.getCommand()));
        data.setCode(0);
        data.setMsg("0");
        cmd.setData(JsonUtils.objectToJson(data));
        session.write(cmd);
    }


    @RequestMapping("" + CmdType.CMD200)
    public void cmd200(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        logger.error(requestData.getToken());
        if (!requestData.getToken().equals("game-wzqgm")) {
            logger.error(requestData.getToken().equals("game-wzqgm") + "");
            return;
        }

        Request200 request = JsonUtils.jsonToPojo(requestData.getParams(), Request200.class);
        ResponseData responseData = new ResponseData(requestData.getAction());
        Response200 response = new Response200();
        if (request.getRoomId() > 10) {
            RoomInfo roomInfo = RoomContext.getRoomByRoomId(request.getRoomId());
            if (roomInfo != null) {
                Vo200 vo200 = new Vo200();
                vo200.setAnonymous(roomInfo.getAnonymous());
                vo200.setGameNum(roomInfo.getGameNum());
                vo200.setRoomId(roomInfo.getRoomId());
                vo200.setShowName(roomInfo.getShowName());
                vo200.setTime(roomInfo.getCreateTime());
                vo200.setType(roomInfo.getType());
                vo200.setAppointment(roomInfo.getAppointment() > 0 ? 1 : 2);
                response.getRooms().add(vo200);
            }
        } else {
            for (RoomInfo roomInfo : RoomContext.getRooms()) {
                Vo200 vo200 = new Vo200();
                vo200.setAnonymous(roomInfo.getAnonymous());
                vo200.setAppointment(roomInfo.getAppointment() > 0 ? 1 : 2);
                vo200.setGameNum(roomInfo.getGameNum());
                vo200.setRoomId(roomInfo.getRoomId());
                vo200.setShowName(roomInfo.getShowName());
                vo200.setTime(roomInfo.getCreateTime());
                vo200.setType(roomInfo.getType());
                response.getRooms().add(vo200);
            }
        }
        responseData.setParams(response);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }

    @RequestMapping("" + CmdType.CMD201)
    public void cmd201(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        if (!requestData.getToken().equals("game-wzqgm")) {
            return;
        }

        Request201 request = JsonUtils.jsonToPojo(requestData.getParams(), Request201.class);
        ResponseData responseData = new ResponseData(requestData.getAction());
        Response201 response = new Response201();
        if (request.getName().equals("") && request.getPtId().equals("")) {
            for (UserVo userVo : UserContext.getUsers().values()) {
                if (!userVo.isOnline()) {
                    continue;
                }
                Vo201 vo200 = new Vo201();
                vo200.setLoginTime(userVo.getLoginTime().getTime());
                vo200.setName(userVo.getUserName());
                vo200.setPtId(Integer.parseInt(userVo.getPtId()));
                vo200.setRoomId(userVo.getRoomId());
                response.getUsers().add(vo200);
            }
        } else {
            if (!request.getPtId().equals("")) {
                UserVo userVo = UserContext.getUser(request.getPtId());
                if (userVo != null && userVo.isOnline()) {
                    Vo201 vo200 = new Vo201();
                    vo200.setLoginTime(userVo.getLoginTime().getTime());
                    vo200.setName(userVo.getUserName());
                    vo200.setPtId(Integer.parseInt(userVo.getPtId()));
                    vo200.setRoomId(userVo.getRoomId());
                    response.getUsers().add(vo200);
                }
            }
            if (!request.getName().equals("")) {
                UserVo userVo = UserContext.getUserByName(request.getName());
                if (userVo != null && userVo.isOnline()) {
                    Vo201 vo200 = new Vo201();
                    vo200.setLoginTime(userVo.getLoginTime().getTime());
                    vo200.setName(userVo.getUserName());
                    vo200.setPtId(Integer.parseInt(userVo.getPtId()));
                    vo200.setRoomId(userVo.getRoomId());
                    response.getUsers().add(vo200);
                }
            }
        }

        responseData.setParams(response);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }

    @RequestMapping("" + CmdType.CMD202)
    public void cmd202(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        if (!requestData.getToken().equals("game-wzqgm")) {
            return;
        }
        Request202 request = JsonUtils.jsonToPojo(requestData.getParams(), Request202.class);
        ResponseData responseData = new ResponseData(requestData.getAction());
        String ptId = request.getPtId() + "";
        List<RankInfoVo> rankInfoVos = SqlManager.getRankInfo(ptId, request.getNum(), request.getPage());
        String sql = "";
        if (ptId.equals("0")) {
            sql = "select a.value1 as num from (select count(*) as value1 from RankInfoVo ) a";
        } else {
            sql = "select a.value1 as num from (select count(*) as value1 from RankInfoVo where ptId='" + ptId + "') a";
        }
        List<NewRankVo> list = SqlManager.getNewRankVos(sql);

        int maxNum = (int) list.get(0).getNum();
        int maxPage = maxNum / request.getNum() + maxNum % request.getNum() != 0 ? 1 : 0;
        Response106 response106 = new Response106();
        response106.setMaxPage(maxPage);
        response106.setThisPage(request.getPage());
        for (RankInfoVo rankInfoVo : rankInfoVos) {
            rankInfoVo.setRankInfoList(ParsingVoUtil.splitString(rankInfoVo.getRankInfo()));
            rankInfoVo.setWinInfoList(ParsingVoUtil.splitWinString(rankInfoVo.getWinInfo()));
            Vo106 vo106 = new Vo106();
            vo106.setGameTime(rankInfoVo.getTime());
            vo106.setPan(rankInfoVo.getPan());
            int i = 0;
            for (WinInfo winInfo : rankInfoVo.getWinInfoList()) {
                Vo106_1 vo106_1 = new Vo106_1();
                vo106_1.setGameTime(winInfo.getGameTime());
                for (RankInfo rankInfo : rankInfoVo.getRankInfoList()) {
                    Vo106_2 vo106_2 = new Vo106_2();
                    vo106_2.setUserName(rankInfo.getUserName());
                    vo106_2.setPtId(rankInfo.getPtId());
                    int status = 3;
                    int integral = rankInfo.getResults().get(i);

                    //1：胜利 2：失败  3：平局
                    if (rankInfo.getPtId().equals(winInfo)) {
                        status = 1;
                    } else {
                        if (!winInfo.getPtId().equals("0")) {
                            status = 2;
                        }
                    }

                    vo106_2.setStatus(status);
                    vo106_2.setIntegral(integral);
                    vo106_1.getPlayInfo().add(vo106_2);
                }
                i++;
                vo106.getInfo().add(vo106_1);
            }
            response106.getRankInfos().add(vo106);
        }
        responseData.setParams(response106);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }

    @RequestMapping("" + CmdType.CMD203)
    public void cmd203(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        if (!requestData.getToken().equals("game-wzqgm")) {
            return;
        }

        NoticeContext.init();
        ResponseData responseData = new ResponseData(requestData.getAction());
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }


}