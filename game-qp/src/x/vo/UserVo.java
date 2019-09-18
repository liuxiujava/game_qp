package x.vo;

import jsa.ioc.annotation.*;
import jsa.orm.vo.JsaVo;

import java.util.Date;


@Entity
public class UserVo extends JsaVo {
    @Indexed(value = "idx_UserVo_ptId", unique = true)
    @Length(6)
    private int ptId;    //6位数字ID
    @Indexed(value = "idx_UserVo_phone", unique = false)
    @Length(100)
    private String account; //wx 账号

    @Indexed(value = "idx_UserVo_phone", unique = false)
    @Length(11)
    private String phone; //绑定手机号

    @Length(60)
    private String passWd;
    @Length(30)
    @Indexed(value = "idx_UserVo_userName", unique = false)
    private String userName;
    private String sfzNum;   //身份证
    @Length(200)
    @Default("1")
    private String headId;   //头像
    @Default("10")
    private int diamond = 10;   //钻石
    @Default("0")
    private int roomId;   //房间Id
    private Date exitTime;  //上一次下线时间
    private Date loginTime;  //最近一次登录时间
    private Date createTime = new Date();  //注册时间
    @Default("0")
    private int fxNum; // 今日分享次数
    @Default("0")
    private long fxTime; //上次分享时间
    @Length(6)
    @Default("0")
    private int status = 0;   // 0:未实名  1：实名
    @Length(1)
    @Default("0")
    private int sex = 0;   // 1：男 2：女

    @NotSaved
    private String token;
    @NotSaved
    private int index;

    @NotSaved
    private boolean online = true;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPtId(int ptId) {
        this.ptId = ptId;
    }

    public String getPassWd() {
        return passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getFxNum() {
        return fxNum;
    }

    public void setFxNum(int fxNum) {
        this.fxNum = fxNum;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public long getFxTime() {
        return fxTime;
    }

    public void setFxTime(long fxTime) {
        this.fxTime = fxTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPtId() {
        return ptId;
    }

    public String getSfzNum() {
        return sfzNum;
    }

    public void setSfzNum(String sfzNum) {
        this.sfzNum = sfzNum;
    }
}
