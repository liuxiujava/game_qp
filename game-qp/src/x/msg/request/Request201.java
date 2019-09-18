package x.msg.request;

public class Request201 {

    private String ptId; // 6位数的 玩家ID   没有数据 “”

    private String name; // 玩家姓名  没有数据 “”


    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
