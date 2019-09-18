package x.game;

import jsa.log.Logger;
import jsa.log.LoggerFactory;
import x.action.UserAction;
import x.executor.ExecutorDispatcher;

public class CommResult {
    final static Logger logger = LoggerFactory.getLogger(CommResult.class, ExecutorDispatcher.LogActor);

    private Point point;
    private int pos;
    private int status;
    private int count;

    public CommResult(Point point, int i, int status, int count) {
        this.point = point;
        this.pos = i;
        this.status = status;
        this.count = count;

        logger.error(pos + "_CommResult_" + count + "__" + status);
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
