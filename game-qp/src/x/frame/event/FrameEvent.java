package x.frame.event;

import jsa.event.Event;
import jsa.event.EventDispatcher;
import x.game.RoomInfo;
import x.vo.UserVo;


public class FrameEvent extends Event<Integer> {
    public final static String SceneSync_Act_Task = "SceneSync_Act_Task";

    private RoomInfo roomInfo;
    private UserVo userVo;

    public FrameEvent(String type, Integer frameIndex) {
        super(type, frameIndex);
    }


    public static String getSceneSync_Act_Task() {
        return SceneSync_Act_Task;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    @Override
    public EventDispatcher getDispatcher() {
        return super.getDispatcher();
    }
}
