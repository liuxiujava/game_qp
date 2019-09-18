package x.msg.imp;


import java.util.ArrayList;
import java.util.List;

public class Vo106 {
    private long gameTime;   //秒
    private int pan;  //总盘数
    private List<Vo106_1> info = new ArrayList<>();


    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    public int getPan() {
        return pan;
    }

    public void setPan(int pan) {
        this.pan = pan;
    }

    public List<Vo106_1> getInfo() {
        return info;
    }

    public void setInfo(List<Vo106_1> info) {
        this.info = info;
    }
}
