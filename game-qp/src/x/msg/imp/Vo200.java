package x.msg.imp;


public class Vo200 {
    private int roomId;
    private int type; // 房间类型 0 人人 1-4 经典
    private int showName;  //1: 显示  2：不显示
    private int gameNum = 1;   //1-3 盘
    private int anonymous = 1;  //是否匿名玩法   1：是  2： 不是
    private int appointment = 1;  //是否启动开局约定  1：启动 2：不启用
    private long time; //创建时间

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getShowName() {
        return showName;
    }

    public void setShowName(int showName) {
        this.showName = showName;
    }

    public int getGameNum() {
        return gameNum;
    }

    public void setGameNum(int gameNum) {
        this.gameNum = gameNum;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    public int getAppointment() {
        return appointment;
    }

    public void setAppointment(int appointment) {
        this.appointment = appointment;
    }



}
