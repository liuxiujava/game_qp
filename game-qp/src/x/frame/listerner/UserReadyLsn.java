package x.frame.listerner;

import jsa.event.EventListener;
import ws.nett.message.IMessage;
import ws.nett.message.Message;
import x.context.RoomContext;
import x.frame.event.RoomEvent;
import x.game.RoomInfo;
import x.msg.ResponseData;
import x.msg.response.Response113;
import x.utils.CmdType;
import x.utils.JsonUtils;

public class UserReadyLsn implements EventListener<RoomEvent> {
    @Override
    public void onEvent(RoomEvent frameEvent) throws Exception {
        RoomInfo roomInfo = frameEvent.getRoomInfo();

        IMessage cmd = new Message(CmdType.CMD113 + "");
        ResponseData responseData = new ResponseData(CmdType.CMD113);
        Response113 response = new Response113();
        response.setPtId(frameEvent.getPtId());
        responseData.setCode(0);
        responseData.setMsg("准备");
        responseData.setParams(response);
        cmd.setData(JsonUtils.objectToJson(responseData));
        RoomContext.responseAll(roomInfo, cmd);

        roomInfo.isReady(frameEvent.getPtId());
    }
}
