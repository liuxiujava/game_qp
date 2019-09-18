package x.msg.imp;


import java.util.ArrayList;
import java.util.List;

public class Vo106_1 {
    private long gameTime;   //结算时间
    private List<Vo106_2> playInfo = new ArrayList<>();

    public List<Vo106_2> getPlayInfo() {
        return playInfo;
    }

    public void setPlayInfo(List<Vo106_2> playInfo) {
        this.playInfo = playInfo;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }
}
