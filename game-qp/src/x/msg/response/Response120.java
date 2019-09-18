package x.msg.response;

public class Response120 {
    private String ptId;
    private int type;//1 = 上线，2 = 离线

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
