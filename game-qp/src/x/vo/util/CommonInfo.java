package x.vo.util;

public class CommonInfo {

    private int id;
    private int value;

    public CommonInfo() {
    }

    public CommonInfo(int type, int value) {
        this.id = type;
        this.value = value;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
