package x.msg.response;


import x.msg.imp.Vo106;

import java.util.ArrayList;
import java.util.List;

public class Response106 {
    private List<Vo106> rankInfos = new ArrayList<>();
    private int thisPage;
    private int maxPage;


    public int getThisPage() {
        return thisPage;
    }

    public void setThisPage(int thisPage) {
        this.thisPage = thisPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public List<Vo106> getRankInfos() {
        return rankInfos;
    }

    public void setRankInfos(List<Vo106> rankInfos) {
        this.rankInfos = rankInfos;
    }
}
