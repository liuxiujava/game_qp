package x.msg.request;

public class Request202 {
    public int ptId;  //玩家Id
    private int num;  //一页显示条数
    private int page;   //0 开始

    public int getPtId() {
        return ptId;
    }

    public void setPtId(int ptId) {
        this.ptId = ptId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
