package x.frame;

import jsa.event.EventContainer;
import x.frame.event.FrameEvent;
import x.frame.event.RoomEvent;
import x.frame.listerner.ExitRoomLsn;
import x.frame.listerner.JoinRoomLsn;
import x.frame.listerner.RoomSyncListener;
import x.frame.listerner.UserReadyLsn;


public class DispatcherContext {
    final public static void init() {
        FrameDispatcher frameDispatcher = new FrameDispatcher();
        frameDispatcher.addEventListener(FrameEvent.SceneSync_Act_Task, new RoomSyncListener());
        EventContainer.addEventDispatcher(frameDispatcher);

        RoomDispatcher roomDispatcher = new RoomDispatcher();
        roomDispatcher.addEventListener(RoomEvent.ExitRoom, new ExitRoomLsn());
        roomDispatcher.addEventListener(RoomEvent.JoinRoom, new JoinRoomLsn());
        roomDispatcher.addEventListener(RoomEvent.UserReady, new UserReadyLsn());
        EventContainer.addEventDispatcher(roomDispatcher);


    }
}
