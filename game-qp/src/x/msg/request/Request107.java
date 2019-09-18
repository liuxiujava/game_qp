package x.msg.request;

public class Request107 {
    //    请求参数：局数、房费、人数、转弯、奖马、坐马、玩法，底分
    private int gameNum = 1;   //局数
    private int ff;  //房费
    private int peopleNum; // 人数
    private int zw; // 转弯 1:大转弯  2：小转弯
    private int jm; // 奖马 0:无奖马 1：赢家奖马 2：马踩马 3： 庄家奖马
    private int jmNum; // 奖马 1-5
    private int zm; // 坐马  1：庄家坐马 2： 庄家不坐马
    private String wf; // 例如选择1和2 用拼接字符串发过来 1,2  玩法 1，可天地胡 2，算假胡 3，马跟杠  4，烧庄  5，解散算杠  6，臭庄马跟杠  7，闲胡马分减半
    private int df; // 底分

    public int getGameNum() {
        return gameNum;
    }

    public void setGameNum(int gameNum) {
        this.gameNum = gameNum;
    }

    public int getFf() {
        return ff;
    }

    public void setFf(int ff) {
        this.ff = ff;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getZw() {
        return zw;
    }

    public void setZw(int zw) {
        this.zw = zw;
    }

    public int getJm() {
        return jm;
    }

    public void setJm(int jm) {
        this.jm = jm;
    }

    public int getZm() {
        return zm;
    }

    public void setZm(int zm) {
        this.zm = zm;
    }

    public int getJmNum() {
        return jmNum;
    }

    public void setJmNum(int jmNum) {
        this.jmNum = jmNum;
    }

    public String getWf() {
        return wf;
    }

    public void setWf(String wf) {
        this.wf = wf;
    }

    public int getDf() {
        return df;
    }

    public void setDf(int df) {
        this.df = df;
    }
}
