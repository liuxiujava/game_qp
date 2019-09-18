package x.msg.response;

public class Response123 {

    private String fzPtId; //0 加入  1：离开  离开的时候  User 对象只填充ptId

    public String getFzPtId() {
        return fzPtId;
    }

    public void setFzPtId(String fzPtId) {
        this.fzPtId = fzPtId;
    }
}
