package x.vo;

import jsa.ioc.annotation.Default;
import jsa.ioc.annotation.Entity;
import jsa.ioc.annotation.Indexed;
import jsa.ioc.annotation.Length;
import jsa.orm.vo.JsaVo;

import java.sql.Date;

@Entity
public class ParamsVo extends JsaVo {
    @Default("0")
    @Length(30)
    private String param_name;
    @Indexed(value = "idx_ParamsVo_param_key", unique = true)
    @Default("0")
    private int param_key;
    @Length(40)
    @Default("")
    private String param_value;
    @Length(30)
    @Default("")
    private String param_desc;
    private Date create_time;

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public int getParam_key() {
        return param_key;
    }

    public void setParam_key(int param_key) {
        this.param_key = param_key;
    }

    public String getParam_value() {
        return param_value;
    }

    public void setParam_value(String param_value) {
        this.param_value = param_value;
    }

    public String getParam_desc() {
        return param_desc;
    }

    public void setParam_desc(String param_desc) {
        this.param_desc = param_desc;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
