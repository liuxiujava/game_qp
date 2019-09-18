package x.frame.listerner;

import jsa.event.EventListener;
import x.context.RoomContext;
import x.frame.event.RoomEvent;
import x.game.RoomInfo;

public class ExitRoomLsn implements EventListener<RoomEvent> {
    @Override
    public void onEvent(RoomEvent frameEvent) throws Exception {
        RoomInfo roomInfo = frameEvent.getRoomInfo();
        RoomContext.exitUser(roomInfo, frameEvent.getUserVo());
    }
}
