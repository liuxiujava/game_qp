package x.msg.response;


public class Response109 {

    private long teaHouseId;  //cg id
    private String teaHouseName;  // cg 名字
    private int roomNum;   // cg 里的房间数

    public long getTeaHouseId() {
        return teaHouseId;
    }

    public void setTeaHouseId(long teaHouseId) {
        this.teaHouseId = teaHouseId;
    }

    public String getTeaHouseName() {
        return teaHouseName;
    }

    public void setTeaHouseName(String teaHouseName) {
        this.teaHouseName = teaHouseName;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }
}
