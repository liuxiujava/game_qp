package x.msg.response;

public class Response121 {

    private int type; //0 加入  1：离开  离开的时候  User 对象只填充ptId
    private UserResponse user;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
