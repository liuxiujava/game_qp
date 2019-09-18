package x.msg.response;

import x.msg.imp.Vo200;

import java.util.ArrayList;
import java.util.List;

public class Response200 {
    private List<Vo200> rooms = new ArrayList<>();

    public List<Vo200> getRooms() {
        return rooms;
    }

    public void setRooms(List<Vo200> rooms) {
        this.rooms = rooms;
    }
}
