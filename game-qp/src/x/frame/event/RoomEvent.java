package x.frame.event;

import ws.nett.session.IoSession;
import x.game.RoomInfo;
import x.vo.UserVo;

public class RoomEvent extends JsaEvent {
    public final static String ExitRoom = "ExitRoom";
    public final static String JoinRoom = "JoinRoom";
    public final static String UserReady = "UserReady";
    private RoomInfo roomInfo;
    private int roomId;
    private UserVo userVo;
    private String ptId;

    public RoomEvent(String type, IoSession source) {
        super(type, source);
    }

    public static String getExitRoom() {
        return ExitRoom;
    }

    public static String getJoinRoom() {
        return JoinRoom;
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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }


    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }
}
