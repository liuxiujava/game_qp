package x.vo;

import jsa.ioc.annotation.Default;
import jsa.ioc.annotation.Entity;
import jsa.ioc.annotation.Length;
import jsa.orm.vo.JsaVo;

import java.util.Date;

@Entity
public class NoticeVo extends JsaVo {
    @Length(30)
    @Default("")
    private String title;
    @Length(50)
    @Default("")
    private String context;

    private Date createTime;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
