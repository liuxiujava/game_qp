package x.frame.listerner;

import jsa.event.EventListener;
import ws.nett.message.IMessage;
import ws.nett.session.IoSession;
import x.context.RoomContext;
import x.db.SqlManager;
import x.executor.ExecutorDispatcher;
import x.frame.event.RoomEvent;
import x.game.RoomInfo;
import x.msg.ResponseData;
import x.msg.response.Response109;
import x.utils.JsonUtils;
import x.vo.UserVo;

public class JoinRoomLsn implements EventListener<RoomEvent> {
    @Override
    public void onEvent(RoomEvent frameEvent) throws Exception {
        RoomInfo roomInfo = frameEvent.getRoomInfo();
        IMessage cmd = frameEvent.getMessage();
        UserVo userVo = frameEvent.getUserVo();
        IoSession session = frameEvent.getSource();
        ResponseData responseData = new ResponseData(Integer.parseInt(cmd.getCommand()));
        if (userVo.getRoomId() != 0 && roomInfo.getRoomId() != userVo.getRoomId()) {
            responseData.setCode(4);
            responseData.setMsg("已在房间 不能重复加入");
            cmd.setData(JsonUtils.objectToJson(responseData));
            session.write(cmd);
            return;
        }

        IPlayer user = roomInfo.getUserByPtId(userVo.getPtId());
        if (user == null) {
            if (roomInfo.getUsers().size() == 3) {
                responseData.setCode(2);
                responseData.setMsg("房间人数已满");
                cmd.setData(JsonUtils.objectToJson(responseData));
                session.write(cmd);
                return;
            }
            user = roomInfo.addUser(userVo);
        } else {
            user.setOnline(1);

            if (userVo.getRoomId() != roomInfo.getRoomId()) {
                userVo.setRoomId(roomInfo.getRoomId());

                SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "roomId");
            }
        }


        Response109 response109 = RoomContext.getResponse109(roomInfo);
        responseData.setParams(response109);
        responseData.setTrue();

        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);

        RoomContext.joinAndExitRoom(roomInfo, user, 0);
    }
}
