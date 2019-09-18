package x.msg.response;

public class Response112 {
    private String ptId; // 发送者
    private String fptId; // 接收者ID
    private String message;

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFptId() {
        return fptId;
    }

    public void setFptId(String fptId) {
        this.fptId = fptId;
    }
}
