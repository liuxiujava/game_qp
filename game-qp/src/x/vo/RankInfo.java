package x.vo;

import x.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class RankInfo {

    private String userName; //昵称
    private String ptId;
    private String result;

    private List<Integer> results = new ArrayList<>();

    public RankInfo(String userName, String ptId) {
        this.userName = userName;
        this.ptId = ptId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;

        for (String str : StringUtil.splitString(result, "_")) {
            if (!str.equals("")) {
                this.results.add(Integer.parseInt(str));
            }
        }
    }

    public List<Integer> getResults() {
        return results;
    }

    public void setResults(List<Integer> results) {
        this.results = results;
    }
}
