package x.action;

import jsa.event.EventContainer;
import jsa.ioc.annotation.Controller;
import jsa.ioc.annotation.RequestMapping;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import ws.nett.message.IMessage;
import ws.nett.session.IoSession;
import x.context.RoomContext;
import x.context.UserContext;
import x.db.SqlManager;
import x.executor.ExecutorDispatcher;
import x.frame.event.RoomEvent;
import x.game.*;
import x.msg.RequestData;
import x.msg.ResponseData;
import x.msg.request.Request107;
import x.msg.request.Request110;
import x.msg.request.Request112;
import x.msg.request.Request116;
import x.msg.response.Response109;
import x.msg.response.Response112;
import x.msg.response.Response118;
import x.msg.response.Response130;
import x.utils.CmdType;
import x.utils.JsonUtils;
import x.vo.UserVo;


@Controller
public class RoomAction {
    final static Logger logger = LoggerFactory.getLogger(x.action.UserAction.class, ExecutorDispatcher.LogActor);

    @RequestMapping("" + CmdType.CMD109)
    public void cmd109(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }

        if (!RoomContext.canJoinGame(0, userVo, cmd, session)) {
            return;
        }

        ResponseData responseData = new ResponseData(requestData.getAction());

        Request107 request = JsonUtils.jsonToPojo(requestData.getParams(), Request107.class);
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setRoomId(RoomContext.getRoomId());
        roomInfo.setShowName(request.getShowName());
        roomInfo.setChuZi(request.getChuZi());
        roomInfo.setGameNum(request.getGameNum());
        roomInfo.setTime(request.getTime());
        roomInfo.setAnonymous(request.getAnonymous());
        roomInfo.setAppointmentNum(request.getAppointmentNum());
        roomInfo.setAppointment(request.getAppointment());
        roomInfo.setCreateTime(System.currentTimeMillis());
        RoomContext.addRoom(roomInfo);
        roomInfo.addUser(userVo);

        if (roomInfo.getAnonymous() == 2) {
            roomInfo.addRobot();
        }

        Response109 response109 = RoomContext.getResponse109(roomInfo);

        responseData.setParams(response109);
        responseData.setTrue();
        responseData.setMsg("创建成功");
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);

    }

    @RequestMapping("" + CmdType.CMD110)
    public void cmd110(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }


        Request110 request = JsonUtils.jsonToPojo(requestData.getParams(), Request110.class);
        if (!RoomContext.canJoinGame(request.getRoomId(), userVo, cmd, session)) {
            return;
        }
        ResponseData responseData = new ResponseData(requestData.getAction());
        RoomInfo roomInfo = null;
        if (request.getRoomId() > 4) {
            roomInfo = RoomContext.getRoomByRoomId(request.getRoomId());
            if (roomInfo == null) {
                responseData.setCode(1);
                responseData.setMsg("房间不存在");
                cmd.setData(JsonUtils.objectToJson(responseData));
                session.write(cmd);
                return;
            }
        } else {

            roomInfo = RoomContext.getRoomByType(request.getRoomId());
        }


        RoomEvent event = new RoomEvent(RoomEvent.JoinRoom, session);
        event.setRoomInfo(roomInfo);
        event.setRoomId(request.getRoomId());
        event.setUserVo(userVo);
        event.setMessage(cmd);
        EventContainer.dispatchEvent(event);

    }


    @RequestMapping("" + CmdType.CMD112)
    public void cmd112(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }
        Request112 request = JsonUtils.jsonToPojo(requestData.getParams(), Request112.class);
        RoomInfo roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());

        if (roomInfo == null) {
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(1);
            responseData.setMsg("不再房间里不能聊天");
            return;
        }

        ResponseData responseData = new ResponseData(CmdType.CMD112);
        Response112 response = new Response112();
        response.setPtId(userVo.getPtId());
        response.setMessage(request.getMessage());
        response.setFptId(request.getPtId());
        responseData.setParams(response);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        for (IPlayer user : roomInfo.getUsers()) {
            if (user.getOnline() != 1 || user instanceof RobotUser) {
                continue;
            }
            if (user.getPtId().equals(userVo.getPtId()) || request.getPtId().equals("") || user.getPtId().equals(request.getPtId())) {
                UserContext.getSessionByPtId(user.getPtId()).write(cmd);
            }
        }


    }


    @RequestMapping("" + CmdType.CMD113)
    public void cmd113(IoSession session, IMessage cmd) throws Exception {
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
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }

        IPlayer iPlayer = roomInfo.getUser(userVo.getPtId());
        if (iPlayer.getIsReady() == 2) {
            RoomEvent event = new RoomEvent(RoomEvent.UserReady, session);
            event.setRoomInfo(roomInfo);
            event.setPtId(userVo.getPtId());
            event.setMessage(cmd);
            EventContainer.dispatchEvent(event);
        }
    }


    @RequestMapping("" + CmdType.CMD116)
    public void cmd116(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }
        Request116 request = JsonUtils.jsonToPojo(requestData.getParams(), Request116.class);
        RoomInfo roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());
        if (roomInfo == null) {
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(1);
            responseData.setMsg("不再房间里不能操作");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }

        if (!roomInfo.getChuziPtId().equals(userVo.getPtId())) {
            logger.error(userVo.getRoomId() + " 不该你出子 " + userVo.getUserName());
            return;
        }

        Point point = new Point(request.getX(), request.getY());
        if (!roomInfo.getQizi().contains(point)) {
            logger.error(roomInfo.getRoomId() + " 该子不能下" + userVo.getRoomId() + "+ " + userVo.getUserName());
            ResponseData responseData = new ResponseData(requestData.getAction());
            responseData.setCode(6);
            responseData.setMsg("该位子已经有子了");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }

        IPlayer user = roomInfo.getUser(userVo.getPtId());
        user.run(point);

    }


    @RequestMapping("" + CmdType.CMD118)
    public void cmd118(IoSession session, IMessage cmd) throws Exception {
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
            responseData.setMsg("房间不存在");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        } else {
            /*if (roomInfo.getStatus() == 1 || roomInfo.getStatus() == 3) {
                ResponseData responseData = new ResponseData(requestData.getAction());
                responseData.setCode(2);
                responseData.setMsg("游戏中无法离开");
                cmd.setData(JsonUtils.objectToJson(responseData));
                session.write(cmd);
                return;
            }*/

            if (roomInfo.getStatus() == 2 || roomInfo.getStatus() == 0) {
                userVo.setRoomId(0);
                SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "roomId");
            }
        }


        ResponseData responseData = new ResponseData(CmdType.CMD118);
        Response118 response118 = new Response118();
        response118.setPtId(userVo.getPtId());
        responseData.setParams(response118);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        RoomContext.responseAll(roomInfo, cmd);

        RoomContext.exitRoom(roomInfo, userVo);

    }

    @RequestMapping("" + CmdType.CMD130)
    public void CMD130(IoSession session, IMessage cmd) throws Exception {
        RequestData requestData = JsonUtils.jsonToPojo(cmd.getData(), RequestData.class);
        UserVo userVo = UserContext.checkLand(session, requestData.getToken());
        if (userVo == null) {
            cmd.setCommand(CmdType.CMD199 + "");
            cmd.setData(UserContext.getResponse());
            session.write(cmd);
            return;
        }

        ResponseData responseData = new ResponseData(Integer.parseInt(cmd.getCommand()));
        Response130 response130 = new Response130();
        response130.setRoomId(userVo.getRoomId());
        responseData.setParams(response130);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }

}
