package x.context;

import jsa.log.Logger;
import jsa.log.LoggerFactory;
import ws.nett.message.IMessage;
import ws.nett.message.Message;
import ws.nett.session.IoSession;
import x.executor.ExecutorDispatcher;
import x.game.*;
import x.msg.ResponseData;
import x.msg.imp.*;
import x.msg.response.*;
import x.utils.CmdType;
import x.utils.JsonUtils;
import x.vo.UserVo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomContext {
    private static final Logger logger = LoggerFactory.getLogger(RoomContext.class, ExecutorDispatcher.LogActor);

    public static final int StartId = 100000;
    public static final int PiPeiTime = 10 * 1000;
    public static final int MaxX = 24;
    public static final int MaxY = 24;
    private static final AtomicInteger idCreator = new AtomicInteger(0);
    public static ConcurrentMap<Integer, RoomInfo> rooms = new ConcurrentHashMap<>();
    public static ConcurrentMap<Integer, List<RoomInfo>> jinDianRoom = new ConcurrentHashMap<>();
    public final static int[][] posArr = {{4, 4}, {12, 4}, {20, 4}, {4, 12}, {12, 12}, {20, 12}, {4, 20}, {12, 20}, {20, 20}};

    public final static int[][] subPos = {{1, 0}, {1, 1}, {0, 1}, {-1, 0}, {-1, 1}, {-1, -1}, {0, -1}, {1, -1}, {0, 0},
            {2, 0}, {2, 2}, {0, 2}, {-2, 0}, {-2, 2}, {-2, -2}, {0, -2}, {2, -2},

    };

    //    {3, 0}, {3, 3}, {0, 3}, {-3, 0}, {-3, 3}, {-3, -3}, {0, -3}, {3, -3}
    public static Collection<RoomInfo> getRooms() {
        return rooms.values();
    }

    public static RoomInfo getRoomByRoomId(int roomId) {
        return rooms.get(roomId);
    }

    public static void addRoom(RoomInfo roomInfo) {
        rooms.put(roomInfo.getRoomId(), roomInfo);
        if (roomInfo.getType() < 4) {
            List<RoomInfo> list = jinDianRoom.get(roomInfo.getType());
            if (list == null) {
                list = new ArrayList<>();
                jinDianRoom.put(roomInfo.getType(), list);
            }
            list.add(roomInfo);
        }
    }

    public static void removeRoom(RoomInfo roomInfo) {
        rooms.remove(roomInfo.getRoomId());
        if (roomInfo.getType() < 4) {
            jinDianRoom.get(roomInfo.getType()).remove(roomInfo);
        }
    }


    public static RoomInfo getRoomByType(int type) {
        List<RoomInfo> list = jinDianRoom.get(type);
        if (list != null && !list.isEmpty()) {
            for (RoomInfo roomInfo : list) {
                if (roomInfo.haveJoiin()) {
                    return roomInfo;
                }
            }
        }

        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setRoomId(RoomContext.StartId * 2 + RoomContext.getRoomId());
        roomInfo.setShowName(1);
        roomInfo.setChuZi(2);
        roomInfo.setGameNum(1);
        roomInfo.setTime(30);
        roomInfo.setAnonymous(2);
        roomInfo.setType(type);
        if (roomInfo.getType() == 4) {
            roomInfo.setAppointmentNum(6);
            roomInfo.setAppointment(1);
        } else {
            roomInfo.setAppointmentNum(0);
            roomInfo.setAppointment(0);
        }
        addRoom(roomInfo);
        return roomInfo;
    }

    public static int getRoomId() {
        return StartId + idCreator.incrementAndGet();
    }

    public static Response109 getResponse109(RoomInfo roomInfo) {
        Response109 response109 = new Response109();
        response109.setChuziPtId(roomInfo.getChuziPtId());
        response109.setGameNum(roomInfo.getGameNum());
        response109.setRoomId(roomInfo.getRoomId());
        response109.setThisNum(roomInfo.getThisNum() == 0 ? 1 : roomInfo.getThisNum());
        if (roomInfo.getChuziTime() != 0) {
            response109.setTime((int) ((roomInfo.getChuziTime() - System.currentTimeMillis()) / 1000));
        } else {
            response109.setTime(roomInfo.getTime());
        }
        response109.setFzPtId(roomInfo.getFzPtId());
        response109.setStatus(roomInfo.getStatus());
        response109.setType(roomInfo.getType());
        response109.setCd(roomInfo.getTime());
        response109.setAppointment(roomInfo.getAppointment());
        response109.setAnonymous(roomInfo.getAnonymous());
        response109.setShowName(roomInfo.getShowName());
        response109.setAppointmentNum(roomInfo.getAppointmentNum());
        for (IPlayer iPlayer : roomInfo.getUsers()) {
            response109.getUsers().add(new UserResponse(iPlayer, roomInfo));
        }

        return response109;
    }

    public static void responseAll(RoomInfo roomInfo, IMessage cmd) {
        for (IPlayer user : roomInfo.getUsers()) {
            if (user instanceof RobotUser) {
                continue;
            }
            if (user.getOnline() != 1) {
                continue;
            }
            UserContext.getSessionByPtId(user.getPtId()).write(cmd);
        }
    }


    public static int getLength(Point point, int x1, int y1) {
        int x = Math.abs(point.x - x1);
        int y = Math.abs(point.y - y1);
        return x > y ? x : y;
    }


    public static void responseAll(RoomInfo roomInfo, IMessage cmd, String ptId) {
        for (IPlayer user : roomInfo.getUsers()) {
            if (user instanceof RobotUser) {
                continue;
            }
            if (user.getOnline() != 1 || user.getPtId().equals(ptId)) {
                continue;
            }

            UserContext.getSessionByPtId(user.getPtId()).write(cmd);
        }
    }


    public static void exitRoom(RoomInfo roomInfo, UserVo userVo) {

        if (roomInfo.getStatus() == 0) {
            IPlayer user = roomInfo.removeUser(userVo);
            if (!roomInfo.hasUser()) {
                //房间移除
                removeRoom(roomInfo);
            } else {
                if (roomInfo.getFzPtId().equals(user.getPtId())) {
                    roomInfo.setFzPtId(roomInfo.getUsers().get(0).getPtId());
                    fzChangeResponse(roomInfo);
                }
                RoomContext.joinAndExitRoom(roomInfo, user, 1);
            }
        } else {
            IPlayer user = roomInfo.getUser(userVo.getPtId());
            if (user != null) {
                user.setOnline(2);
            }
        }
    }

    public static void fzChangeResponse(RoomInfo roomInfo) {
        IMessage cmd = new Message(CmdType.CMD123 + "");
        ResponseData responseData = new ResponseData(CmdType.CMD123);
        Response123 response = new Response123();
        response.setFzPtId(roomInfo.getFzPtId());
        responseData.setCode(0);
        responseData.setMsg("房主变更");
        responseData.setParams(response);
        cmd.setData(JsonUtils.objectToJson(responseData));
        RoomContext.responseAll(roomInfo, cmd);
    }


    public static void exitUser(RoomInfo roomInfo, UserVo userVo) {
        exitRoom(roomInfo, userVo);

        IMessage cmd = new Message(CmdType.CMD120 + "");
        ResponseData responseData = new ResponseData(CmdType.CMD120);
        Response120 response = new Response120();
        response.setPtId(userVo.getPtId());
        response.setType(2);
        responseData.setParams(response);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        responseAll(roomInfo, cmd);
    }

    public static void upLogin(RoomInfo roomInfo, UserVo userVo) {
        IMessage cmd = new Message(CmdType.CMD120 + "");
        ResponseData responseData = new ResponseData(CmdType.CMD120);
        Response120 response = new Response120();
        response.setPtId(userVo.getPtId());
        response.setType(1);
        responseData.setParams(response);
        responseData.setTrue();
        cmd.setData(JsonUtils.objectToJson(responseData));
        responseAll(roomInfo, cmd, userVo.getPtId());
    }


    public static void xiaziResponse(IPlayer user, RoomInfo roomInfo, Point point) {
        IPlayer iPlayer = roomInfo.getNextPlay(user);
        roomInfo.setChuziPtId(iPlayer);
//        roomInfo.setChuziPtId(user);
        IMessage cmd = new Message(CmdType.CMD115 + "");
        ResponseData responseData = new ResponseData(CmdType.CMD115);
        Response115 response = new Response115();
        response.setPtId(user.getPtId());
        response.setPoint(new Point(point.getX(), point.getY(), point.getIndex()));
        response.setNextPtId(roomInfo.getChuziPtId());
        response.setTime(roomInfo.getTime());
        responseData.setCode(0);
        responseData.setMsg("下子成功");
        responseData.setParams(response);
        cmd.setData(JsonUtils.objectToJson(responseData));
        RoomContext.responseAll(roomInfo, cmd);

        logger.error(user.getRoomInfo().getRoomId() + " 下子成功" + user.getPtId() + "_" + user.getPos() + "_x=" + point.getX() + " y=" + point.getY());
    }
 

    public static void winResponse(RoomInfo roomInfo, String winPtId) {
        IMessage cmd = new Message(CmdType.CMD119 + "");
        ResponseData responseData = new ResponseData(CmdType.CMD119);
        Response119 response = new Response119();
        response.setNum(roomInfo.getThisNum());


        responseData.setCode(0);
        responseData.setMsg("结算信息");
        responseData.setParams(response);
        cmd.setData(JsonUtils.objectToJson(responseData));
        RoomContext.responseAll(roomInfo, cmd);
    }

    public static Vo119 createVo119(IPlayer vo, int value, boolean isEnd) {
        Vo119 vo119 = new Vo119();
        vo119.setName(vo.getRoomInfo().getShowName(vo, isEnd));
        vo119.setHead(vo.getRoomInfo().getHead(vo, isEnd));
        vo119.setPtId(vo.getPtId());
        vo119.setValue(value);
        return vo119;
    }


    public static void joinAndExitRoom(RoomInfo roomInfo, IPlayer iPlayer, int type) {
        IMessage cmd = new Message(CmdType.CMD121 + "");
        ResponseData responseData = new ResponseData(CmdType.CMD121);
        Response121 response121 = new Response121();
        response121.setType(type);
        response121.setUser(new UserResponse(iPlayer, roomInfo));
        responseData.setTrue();
        responseData.setParams(response121);
        cmd.setData(JsonUtils.objectToJson(responseData));

        responseAll(roomInfo, cmd, iPlayer.getPtId());
    }


    public static boolean canJoinGame(int roomId, UserVo userVo, IMessage cmd, IoSession session) {
        if (userVo.getRoomId() != 0) {
            RoomInfo roomInfo = RoomContext.getRoomByRoomId(userVo.getRoomId());
            if (roomInfo != null && roomInfo.getStatus() != 2 && roomId != roomInfo.getRoomId()) {
                ResponseData responseData = new ResponseData(Integer.parseInt(cmd.getCommand()));
                responseData.setCode(11);
                responseData.setMsg("之前有房间未结束");
                responseData.setParams(userVo.getRoomId());
                cmd.setData(JsonUtils.objectToJson(responseData));
                session.write(cmd);
                return false;
            } else {
                userVo.setRoomId(0);
            }
        }
        return true;
    }


}
