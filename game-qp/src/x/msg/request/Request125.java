package x.msg.request;

public class Request125 {
    private int type;  // 1:作废 不纳入统计  2：作废纳入统计   按平局

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
