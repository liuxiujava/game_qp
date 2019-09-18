package x.vo;

import jsa.ioc.annotation.Indexed;
import jsa.ioc.annotation.Length;
import jsa.orm.vo.JsaVo;

public class TeaHouseVo extends JsaVo {
    @Indexed(value = "idx_UserVo_ptId", unique = true)
    @Length(6)
    private int ptId;    //6位数字ID
    private long TeaHouseId; //cg标识
    @Length(12)
    private String teaHouseName;
    private long createTime = System.currentTimeMillis();

    public int getPtId() {
        return ptId;
    }

    public void setPtId(int ptId) {
        this.ptId = ptId;
    }

    public long getTeaHouseId() {
        return TeaHouseId;
    }

    public void setTeaHouseId(long teaHouseId) {
        TeaHouseId = teaHouseId;
    }

    public String getTeaHouseName() {
        return teaHouseName;
    }

    public void setTeaHouseName(String teaHouseName) {
        this.teaHouseName = teaHouseName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
