package x.frame;

import jsa.dispatcher.IDispatcher;
import jsa.event.AbsEventDispatcher;
import jsa.event.Event;
import x.executor.ExecutorDispatcher;
import x.frame.event.RoomEvent;


public class RoomDispatcher extends AbsEventDispatcher {

    @Override
    protected IDispatcher getDispatcher(Event evt) {
        RoomEvent event = (RoomEvent) evt;
        return ExecutorDispatcher.getSceneActor(event.getRoomInfo().getRoomId());
    }

    /**
     * 处理异常
     *
     * @param evt
     * @param e
     */
    @Override
    protected void onException(Event evt, Throwable e) {
        e.printStackTrace();
    }

}
