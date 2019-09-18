package x.msg.response;

import x.msg.imp.Vo119;

import java.util.ArrayList;
import java.util.List;

public class Response119 {
    private List<String> winPtId = new ArrayList<>();
    private int num;  //第几局
    private List<Vo119> jsInfo = new ArrayList<>();
    private List<Vo119> jsInfo2= new ArrayList<>();
    private String bigWin;  //大赢家

    public List<Vo119> getJsInfo2() {
        return jsInfo2;
    }

    public void setJsInfo2(List<Vo119> jsInfo2) {
        this.jsInfo2 = jsInfo2;
    }

    public List<Vo119> getJsInfo() {
        return jsInfo;
    }

    public void setJsInfo(List<Vo119> jsInfo) {
        this.jsInfo = jsInfo;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<String> getWinPtId() {
        return winPtId;
    }

    public void setWinPtId(List<String> winPtId) {
        this.winPtId = winPtId;
    }

    public String getBigWin() {
        return bigWin;
    }

    public void setBigWin(String bigWin) {
        this.bigWin = bigWin;
    }
}
