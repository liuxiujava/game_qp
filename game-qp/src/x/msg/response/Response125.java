package x.msg.response;

public class Response125 {
    private String name;   //某某人 发起里求和
    private String ptId;   //某某人 id
    private int type;  // 1:作废 不纳入统计  2：作废纳入统计   按平局

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }
}
