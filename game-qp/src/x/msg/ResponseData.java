package x.msg;

public class ResponseData {

    private Integer action;       // 动作类型
    private Object params;        // 用户的聊天内容entity
    private int code = -1;        // 结果码
    private String msg = "未知错误"; // 扩展字段

    public ResponseData() {

    }

    public void setTrue() {
        this.code = 0;        // 结果码
        this.msg = "请求成功"; // 扩展字段
    }

    public ResponseData(int action) {
        this.action = action;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
