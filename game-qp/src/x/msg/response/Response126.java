package x.msg.response;

public class Response126 {
    private String name;   //某某人 拒绝or 同意求和
    private String ptId;   //某某人 id
    private int value;   // 1:同意  2：拒绝

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }
}
