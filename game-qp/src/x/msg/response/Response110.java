package x.msg.response;


import x.msg.imp.Vo108;

import java.util.ArrayList;
import java.util.List;

public class Response110 {
    private List<Vo108> rankings = new ArrayList<>();
    private int thisPage;
    private int maxPage;

    public List<Vo108> getRankings() {
        return rankings;
    }

    public void setRankings(List<Vo108> rankings) {
        this.rankings = rankings;
    }

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
}
