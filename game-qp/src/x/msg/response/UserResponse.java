package x.msg.response;

import x.game.Point;
import x.game.RoomInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/9/2.
 */
public class UserResponse {
    protected String ptId;
    protected String userName;
    protected String head;
    protected int level = 1;
    protected int integral = 0;
    protected int sex = 1;
    protected int fgNum;  //违规次数
    protected int fgCostNum;  //犯规扣分

    protected int isReady = 2; //1:已准备 2：未准备
    protected List<Point> qizi = new ArrayList<>();
    protected int pos;  //顺序位置
    protected int online = 1; //是否在线（在房间）  1：在房间   2：已离开



    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFgNum() {
        return fgNum;
    }

    public void setFgNum(int fgNum) {
        this.fgNum = fgNum;
    }

    public int getFgCostNum() {
        return fgCostNum;
    }

    public void setFgCostNum(int fgCostNum) {
        this.fgCostNum = fgCostNum;
    }

    public int getIsReady() {
        return isReady;
    }

    public void setIsReady(int isReady) {
        this.isReady = isReady;
    }

    public List<Point> getQizi() {
        return qizi;
    }

    public void setQizi(List<Point> qizi) {
        this.qizi = qizi;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
