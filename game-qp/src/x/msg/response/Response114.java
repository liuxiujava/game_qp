package x.msg.response;

public class Response114 {
    private String chuziPiId;
    private int time;  //倒计时剩余秒
    private int thisNum; //当前第几盘

    public String getChuziPiId() {
        return chuziPiId;
    }

    public void setChuziPiId(String chuziPiId) {
        this.chuziPiId = chuziPiId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getThisNum() {
        return thisNum;
    }

    public void setThisNum(int thisNum) {
        this.thisNum = thisNum;
    }
}
