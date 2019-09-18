package x.msg.request;

import java.io.Serializable;

public class WxLandMessage implements Serializable {
    private String code;        // 帐号

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
