package x.msg.request;

import java.io.Serializable;

public class Request101 implements Serializable {
    private String phone;        // 帐号
    private String pwd;        //  密码

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
}
