package x.msg.response;

import x.msg.imp.Vo201;

import java.util.ArrayList;
import java.util.List;

public class Response201 {
    private List<Vo201> users = new ArrayList<>();

    public List<Vo201> getUsers() {
        return users;
    }

    public void setUsers(List<Vo201> users) {
        this.users = users;
    }
}
