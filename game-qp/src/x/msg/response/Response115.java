package x.msg.response;

import x.game.Point;

public class Response115 {
    private String ptId;
    private Point point;
    private String nextPtId;
    private int time;

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getNextPtId() {
        return nextPtId;
    }

    public void setNextPtId(String nextPtId) {
        this.nextPtId = nextPtId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
