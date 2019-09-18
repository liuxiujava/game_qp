package x.db;

/**
 * User: LiuXiu
 * Date: 2015/8/28
 * Time: 14:37
 * Description:DB执行类型
 */
public enum OperateType {
    ById(1), ByValue(2), BySql(3),SaveOne(4),SaveMore(5);

    private int state;

    OperateType(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
