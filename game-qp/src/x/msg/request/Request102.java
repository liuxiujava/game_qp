package x.msg.request;

import java.io.Serializable;

public class Request102 implements Serializable {
    private String phone;        // 帐号
    private String pwd;        //  密码
    private String code;       //验证码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
