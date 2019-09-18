package x.msg.response;

public class Response118 {
    private String ptId;

    private int type;  //0：游戏未开始 离开队伍  1： 游戏中离开


    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
