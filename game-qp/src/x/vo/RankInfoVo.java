package x.vo;

import jsa.ioc.annotation.*;
import jsa.orm.vo.JsaVo;
import x.vo.util.CommonInfo;
import x.vo.util.CommonStringUtil;

import java.util.ArrayList;
import java.util.List;

@Entity
public class RankInfoVo extends JsaVo {
    @Indexed(value = "idx_RankInfoVo_ptId", unique = false)
    @Length(12)
    private String ptId;
    @Length(12)
    private long time;
    @Default("0")
    private int pan; //总盘数
    @Length(200)
    @Default("")
    private String winInfo;   //胜利人的ptId
    @Length(200)
    @Default("")
    private String rankInfo;   //userName,ptId,time


    @NotSaved
    private List<RankInfo> rankInfoList = new ArrayList<>();
    @NotSaved
    private List<WinInfo> winInfoList = new ArrayList<>();

    public String getWinInfo() {
        return winInfo;
    }

    public void setWinInfo(String winInfo) {
        this.winInfo = winInfo;
    }

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPan() {
        return pan;
    }

    public void setPan(int pan) {
        this.pan = pan;
    }


    public String getRankInfo() {
        return rankInfo;
    }

    public void setRankInfo(String rankInfo) {
        this.rankInfo = rankInfo;
    }

    public List<RankInfo> getRankInfoList() {
        return rankInfoList;
    }

    public void setRankInfoList(List<RankInfo> rankInfoList) {
        this.rankInfoList = rankInfoList;
    }

    public List<WinInfo> getWinInfoList() {
        return winInfoList;
    }

    public void setWinInfoList(List<WinInfo> winInfoList) {
        this.winInfoList = winInfoList;
    }
}
