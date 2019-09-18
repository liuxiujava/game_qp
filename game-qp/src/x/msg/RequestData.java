package x.msg;

import java.io.Serializable;

public class RequestData implements Serializable {

    private static final long serialVersionUID = 8021381444738260454L;

    private Integer action;        // 动作类型
    private String params;    // 用户的聊天内容entity
    private String token;        //

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
