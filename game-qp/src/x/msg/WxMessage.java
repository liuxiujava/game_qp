package x.msg;

import ws.nett.message.Message;
import x.utils.JsonUtils;

public class WxMessage extends Message {

    public void setData(ResponseData data) {

        super.setData(JsonUtils.objectToJson(data));
    }
}
