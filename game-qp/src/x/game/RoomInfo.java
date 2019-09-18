package x.game;

import jsa.log.Logger;
import jsa.log.LoggerFactory;
import jsa.utils.RandomUtil;
import ws.nett.message.IMessage;
import ws.nett.message.Message;
import x.context.RoomContext;
import x.db.SqlManager;
import x.executor.ExecutorDispatcher;
import x.msg.ResponseData;
import x.msg.response.Response114;
import x.utils.CmdType;
import x.utils.JsonUtils;
import x.vo.UserVo;
import x.vo.WinInfo;

import java.util.*;

public class RoomInfo {
    private static final Logger logger = LoggerFactory.getLogger(RoomInfo.class, ExecutorDispatcher.LogActor);
    private int frameIndex;


    private int showName;  //1: 显示 2：不显示
    private int roomId;
    private int chuZi = 1; //出子顺序  1：随机  2 ：循环
    private int gameNum = 1;
    private int time;      //出子倒计时
    private int thisNum = 0;  //当前第几局
    private String chuziPtId;
    private int status = 0;  // 1： 开始  2： 结束  3:等待下一盘
    private List<Point> qizi = new LinkedList<>();
    private List<IPlayer> users = new ArrayList<>();
    private long chuziTime; //下次出子时间
    private long piPeiTime = 0; //匹配时间
    private int anonymous = 1; //是否匿名玩法 1：人人人  2： 人人机
    private int appointment = 1;  //开局约定 基准点数  1  2  3  0
    private int appointmentNum = 3; // 开局约定 每人的棋子数    4--6

    private int type; // 房间类型 0 人人 1-4 经典
    private long createTime;

    private List<WinInfo> wins = new ArrayList<>();
    private Map<String, List<Integer>> rankInfos = new HashMap<>();   //玩家积分列表

    private String fzPtId; //房主

    private int qhType;
    private List<String> qhUser = new ArrayList<>();
    private long qhTime = System.currentTimeMillis();
    private int maxNum;

    public void initBoard(int maxX, int maxY) {
        qizi = new LinkedList<>();
        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                qizi.add(new Point(i, j));
            }
        }
        maxNum = qizi.size();
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public void addFrameIndex() {
        this.frameIndex += 1;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getChuZi() {
        return chuZi;
    }

    public void setChuZi(int chuZi) {
        this.chuZi = chuZi;
    }

    public long getPiPeiTime() {
        return piPeiTime;
    }

    public void setPiPeiTime(long piPeiTime) {
        this.piPeiTime = piPeiTime;
    }

    public List<WinInfo> getWins() {
        return wins;
    }

    public void setWins(List<WinInfo> wins) {
        this.wins = wins;
    }

    public int getGameNum() {
        return gameNum;
    }

    public void setGameNum(int gameNum) {
        this.gameNum = gameNum;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<IPlayer> getUsers() {
        return users;
    }

    public IPlayer addUser(UserVo userVo) {
        User user = new User(userVo, getPos());
        user.setRoomInfo(this);
        this.users.add(user);
        userVo.setRoomId(roomId);
        SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "roomId");
        ExecutorDispatcher.bindingActor(userVo, getRoomId());
        if (fzPtId == null) {
            fzPtId = userVo.getPtId();
        }
        piPeiTime = System.currentTimeMillis() + RoomContext.PiPeiTime;

        return user;
    }

    public String getShowName(IPlayer iPlayer, boolean isEnd) {
        String name = iPlayer.getUserName();
        if (!isEnd) {
            if (showName == 2) {
                if (iPlayer.getPos() == 1) {
                    name = "魏";
                } else if (iPlayer.getPos() == 2) {
                    name = "蜀";
                } else {
                    name = "吴";
                }
            }
        }
        return name;
    }


    public String getHead(IPlayer iPlayer, boolean isEnd) {
        if (!isEnd) {
            if (showName == 2) {
                return iPlayer.getPos() + "";
            }
        }
        return iPlayer.getHead();
    }


    public IPlayer addRobot() {
        RobotUser robotUser = new RobotUser();
        robotUser.setPos(getPos());
        robotUser.setPtId("10" + RandomUtil.getNexInt(7) + 3 + "" + RandomUtil.getNexInt(10) + "" + RandomUtil.getNexInt(10) + "" + RandomUtil.getNexInt(10));
        for (IPlayer iPlayer : users) {
            if (iPlayer.getPtId().equals(robotUser.getPtId())) {
                robotUser.setPtId((Integer.parseInt(robotUser.getPtId()) + RandomUtil.getNexInt(50) + 1) + "");
                break;
            }
        }

        robotUser.setLevel(RandomUtil.getNexInt(5) + 1);
        int jf = 0;
        if (type == 0) {
            jf = RandomUtil.getNexInt(600) + RandomUtil.getNexInt(100);
        } else if (type == 1) {
            jf = RandomUtil.getNexInt(200);
        } else if (type == 2) {
            jf = RandomUtil.getNexInt(1000);
        } else if (type == 3) {
            jf = RandomUtil.getNexInt(4000) + 1000;
        } else if (type == 4) {
            jf = RandomUtil.getNexInt(1000) + RandomUtil.getNexInt(1000) + 1000;
        }

        int sub = jf % 5;
        robotUser.setIntegral(jf - sub);
        robotUser.setIsReady(1);
        robotUser.setSex(RandomUtil.getNexInt(2) + 1);
        String name = "王子";
        int head = 5;
        if (robotUser.getSex() == 1) {
            name = "公主";
            head = 1;
        }
        robotUser.setUserName("" + name);
        robotUser.setHead((RandomUtil.getNexInt(4) + head) + "");

        robotUser.setRoomInfo(this);
        this.users.add(robotUser);
        piPeiTime = System.currentTimeMillis() + RoomContext.PiPeiTime;
        logger.error(getRoomId() + " 加入机器人  ptId=" + robotUser.getPtId() + " pos=" + robotUser.getPos());
        return robotUser;
    }


    public int getPos() {
        for (int i = 1; i < 4; i++) {
            boolean has = false;
            for (IPlayer vo : users) {
                if (vo.getPos() == i) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                return i;
            }
        }
        return -1;
    }


    public void isReady(String ptId) {

        boolean allReady = true;
        for (IPlayer user : users) {
            if (user.getPtId().equals(ptId)) {
                user.setIsReady(1);
            }
            if (user.getIsReady() == 2) {
                allReady = false;
            }
        }

        if (allReady) {
            if (users.size() == 3 && status != 1) {
                start();
            }
        }
    }


    public void start() {

        initBoard(RoomContext.MaxX, RoomContext.MaxY);
        status = 1;
        thisNum += 1;
        qhType = 0;
        qhUser.clear();
        qhTime = 0;

        users.sort(new FunctionComparator());  //排序
        for (IPlayer user : users) {
            user.getQizi().clear();
            user.clearMessage();
        }

        IMessage cmd = new Message(CmdType.CMD114 + "");
        if (chuZi == 1) {
            Collections.shuffle(users); // 混乱的意思
            setChuziPtId(users.get(0));
        } else {
            setChuziPtId(getUserByPos(thisNum));
        }


        ResponseData responseData = new ResponseData(CmdType.CMD114);
        responseData.setTrue();

        Response114 response114 = new Response114();
        response114.setChuziPiId(getChuziPtId());
        response114.setTime(time);
        response114.setThisNum(getThisNum());
        responseData.setParams(response114);

        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));

        RoomContext.responseAll(this, cmd);
    }


    public IPlayer getUser(String ptId) {
        for (IPlayer user : users) {
            if (user.getPtId().equals(ptId)) {
                return user;
            }
        }
        return null;
    }

    public IPlayer removeUser(UserVo userVo) {
        IPlayer player = null;
        for (IPlayer user : users) {
            if (user.getPtId().equals(userVo.getPtId())) {
                users.remove(user);
                player = user;
                break;
            }
        }
        userVo.setRoomId(0);
        SqlManager.updateVo(userVo, ExecutorDispatcher.getDbActor(1), "roomId");
        ExecutorDispatcher.removeBindActor(userVo);

        return player;
    }

    public int getShowName() {
        return showName;
    }

    public void setShowName(int showName) {
        this.showName = showName;
    }

    public int getThisNum() {
        return thisNum;
    }

    public void setThisNum(int thisNum) {
        this.thisNum = thisNum;
    }

    public String getChuziPtId() {
        return chuziPtId;
    }

    public void setChuziPtId(IPlayer iPlayer) {
        this.chuziPtId = iPlayer.getPtId();
        if (iPlayer.getOnline() == 2 || iPlayer instanceof RobotUser) {
            this.chuziTime = System.currentTimeMillis() + RandomUtil.getNexInt(5 * 1000) + 5 * 1000; //todo
        } else {
            this.chuziTime = System.currentTimeMillis() + time * 1000;
        }
    }


    public List<Point> getQizi() {
        return qizi;
    }

    public void setQizi(List<Point> qizi) {
        this.qizi = qizi;
    }

    public long getChuziTime() {
        return chuziTime;
    }

    public void setChuziTime(long chuziTime) {
        this.chuziTime = chuziTime;
    }

    public IPlayer getUserByPtId(String ptId) {
        for (IPlayer vo : users) {
            if (vo.getPtId().equals(ptId)) {
                return vo;
            }
        }
        return null;
    }

    public IPlayer getUserByPos(int pos) {
        for (IPlayer vo : users) {
            if (vo.getPos() == pos) {
                return vo;
            }
        }
        System.out.println(pos + "___________ getUserByPos");
        return null;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public IPlayer getNextPlay(IPlayer user) {
        int i = 0;
        if (chuZi == 1) {   //随机 或者 按list 下标来获取
            for (int x = 0; x < users.size(); x++) {
                if (user == users.get(x)) {
                    i = x;
                    break;
                }
            }
            i += 1;
            if (i > 2) {
                i = 0;
            }
            return users.get(i);
        } else {   //循环 所以按 pos来获取
            i = user.getPos() + 1;
            if (i > 3) {
                i = 1;
            }
            return getUserByPos(i);
        }

    }

    public boolean haveJoiin() {
        if (users.size() < 3) {
            return true;
        }
        return false;
    }

    public boolean hasUser() {
        for (IPlayer user : users) {
            if (!(user instanceof RobotUser)) {
                return true;
            }
        }
        return false;
    }


    public void setqhMessge(int type, String ptId) {
        setQhType(type);
        setQhTime(System.currentTimeMillis());
        getQhUser().add(ptId);

        for (IPlayer iPlayer : users) {
            if (iPlayer instanceof RobotUser) {
                getQhUser().add(iPlayer.getPtId());
            }
        }
    }

    public void updateQhMessage(int value, String ptId) {
        if (value == 1) {
            if (!qhUser.contains(ptId)) {
                qhUser.add(ptId);
            }
        } else {
            setQhType(0);
            qhUser.clear();
            qhTime = 0;
        }
    }

    public List<Point> getJzPoint(String ptId) {
        List<Point> points = new ArrayList<>();
        for (IPlayer iPlayer : users) {
            if (!ptId.equals("")) {
                if (iPlayer.getPtId().equals(ptId)) {
                    return checkJiZhunPoint(iPlayer.getQizi(), 1);
                }
            } else {
                List<Point> list = checkJiZhunPoint(iPlayer.getQizi(), 0);
                if (!list.isEmpty()) {
                    points.add(list.get(0));
                }
            }
        }

        List<Point> temps = new ArrayList<>();
        for (int i = 0; i < RoomContext.posArr.length; i++) {
            int[] pos = RoomContext.posArr[i];
            Point point = new Point(pos[0], pos[1]);
            if (!points.contains(point)) {
                temps.add(point);
            }
        }

        points.clear();
        points.add(temps.get(RandomUtil.getNexInt(temps.size())));
        return points;
    }


    private List<Point> checkJiZhunPoint(List<Point> points, int type) {
        List<Point> temps1 = new ArrayList<>();
        List<Point> temps = new ArrayList<>();
        int posIndex = -1;
        int minLen = 99999;

        for (int i = 0; i < RoomContext.posArr.length; i++) {
            int[] pos = RoomContext.posArr[i];
            int len = 0;
            int other = 0;
            boolean all = true;
            for (Point point : points) {
                int num = RoomContext.getLength(point, pos[0], pos[1]);
                len += num;

                if (num > 5) {
                    other++;
                    all = false;
                }
            }

            int subNum = points.size() - other;
            if (subNum > other) {
                if (len < minLen) {
                    posIndex = i;
                }
                if (type == 1) {
                    temps.add(new Point(pos[0], pos[1]));
                }
            }
            if (all) {
                temps1.add(new Point(pos[0], pos[1]));
            }

        }

        if (type == 0 && posIndex != -1) {
            int[] pos = RoomContext.posArr[posIndex];
            temps.add(new Point(pos[0], pos[1]));
        }

        if (!temps1.isEmpty()) {

            System.out.println("temp1=" + temps1);
            return temps1;
        }

        System.out.println("temps= " + temps);
        return temps;
    }


    private class FunctionComparator implements Comparator<IPlayer> {
        @Override
        public int compare(IPlayer arg0, IPlayer arg1) {
            return arg0.getPos() - arg1.getPos();
        }
    }


    public String checkWin(IPlayer user) {
        if (user == null || user.hasWin() || getQizi().size() == 0) {
            if (thisNum < gameNum) {
                setStatus(3);
                chuziTime = System.currentTimeMillis() + 15 * 1000;
            } else {
                setStatus(2);
            }

            if (user != null && user.hasWin()) {
                return user.getPtId() + "";
            }
            return "0";
        }

        return "-1";
    }

    public boolean qhEnd() {
        for (IPlayer user : users) {
            if (!(user instanceof RobotUser)) {
                if (user.getOnline() == 1) {
                    if (!qhUser.contains(user.getPtId())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFzPtId() {
        return fzPtId;
    }

    public void setFzPtId(String fzPtId) {
        this.fzPtId = fzPtId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Map<String, List<Integer>> getRankInfos() {
        return rankInfos;
    }

    public void setRankInfos(Map<String, List<Integer>> rankInfos) {
        this.rankInfos = rankInfos;
    }


    public int getQhType() {
        return qhType;
    }

    public void setQhType(int qhType) {
        this.qhType = qhType;
    }

    public List<String> getQhUser() {
        return qhUser;
    }

    public void setQhUser(List<String> qhUser) {
        this.qhUser = qhUser;
    }

    public long getQhTime() {
        return qhTime;
    }

    public void setQhTime(long qhTime) {
        this.qhTime = qhTime;
    }

    public int getAppointmentNum() {
        if (appointment != 0 && appointmentNum == 0) {
            appointmentNum = appointment;
        }
        return appointmentNum;
    }

    public void setAppointmentNum(int appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public int getAppointment() {
        return appointment;
    }

    public void setAppointment(int appointment) {
        this.appointment = appointment;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }
}
