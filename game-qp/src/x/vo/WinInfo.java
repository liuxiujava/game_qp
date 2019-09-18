package x.vo;

public class WinInfo {

    private String ptId;
    private long gameTime;

    public WinInfo(String ptId, long gameTime) {
        this.ptId = ptId;
        this.gameTime = gameTime;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }
}
