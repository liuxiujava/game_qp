package x.frame;

import jsa.event.EventContainer;
import jsa.task.ITask;
import x.context.RoomContext;
import x.frame.event.FrameEvent;

public class FrameAct implements ITask {
    private final static String Frame_Act_Counter = "Frame_Act_Counter";

    @Override
    public Object getTaskId() {
        return Frame_Act_Counter;
    }

    @Override
    public void doTask() {
        RoomContext.getRooms().forEach(roomInfo -> {
            FrameEvent event = new FrameEvent(FrameEvent.SceneSync_Act_Task, 0);
            event.setRoomInfo(roomInfo);
            EventContainer.dispatchEvent(event);
        });
    }
}

