package x.context;

import jsa.log.Logger;
import jsa.log.LoggerFactory;
import ws.nett.message.IMessage;
import ws.nett.message.Message;
import ws.nett.session.IoSession;
import x.executor.ExecutorDispatcher;
import x.msg.ResponseData;
import x.msg.response.Response111;
import x.utils.CmdType;
import x.utils.JsonUtils;
import x.utils.KeyUtil;
import x.vo.UserVo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserContext {
    final static Logger logger = LoggerFactory.getLogger(UserContext.class, ExecutorDispatcher.LogActor);
    public static final int START_INDEX = 500000; //起始Index
    public static final int StartId = 100000;
    //key account value ptId
    private static ConcurrentMap<String, Integer> accounts = new ConcurrentHashMap<>();
    //key account value ptId
    private static ConcurrentMap<String, Integer> names = new ConcurrentHashMap<>();

    //key ptId v
    private static ConcurrentMap<Integer, UserVo> users = new ConcurrentHashMap<>();
    //key token value IoSession
    private static ConcurrentMap<String, IoSession> tokens = new ConcurrentHashMap<>();

    public static UserVo checkLand(IoSession session, String token) {
        return UserContext.getUserByToken(session, token);
    }

    public static String getResponse() {
        ResponseData responseData = new ResponseData(CmdType.CMD199);
        responseData.setCode(0);
        responseData.setMsg("登录失效请重新登录");
        return JsonUtils.objectToJson(responseData);
    }


    public static Integer getPtIdByAccount(String account) {
        if (accounts.get(account) != null) {
            return accounts.get(account);
        } else {
            return null;
        }
    }

    public static UserVo getUserByToken(IoSession session, String token) {
        IoSession oldSession = tokens.get(token);
        if (oldSession != null) {
            String ptId = (String) oldSession.getAttribute(KeyUtil.PtId);
            if (session.getSessionId() != oldSession.getSessionId()) {
                oldSession.setAttribute(KeyUtil.OldSession, KeyUtil.OldSession);// 重复登录，先把之前的用户踢出服务器

                IMessage cmd = new Message(CmdType.CMD122 + "");
                ResponseData responseData = new ResponseData(CmdType.CMD122);
                responseData.setCode(1);
                responseData.setMsg("帐号在异地登录");
                cmd.setData(JsonUtils.objectToJson(responseData));
                oldSession.write(cmd);
                logger.error(" oldSession session.close 22222222222");
                oldSession.close(true);
                tokens.put(token, session);
                session.setAttribute(KeyUtil.PtId, ptId);
            }
            if (ptId != null) {
                UserVo userVo = users.get(ptId);
                if (userVo != null) {
                    userVo.setOnline(true);
                }
                return userVo;
            }
        }

        if (session.getAttribute(KeyUtil.PtId) != null) {
            String ptId = (String) session.getAttribute(KeyUtil.PtId);
            return users.get(ptId);
        }
        return null;
    }

    public static IoSession getSessionByPtId(String ptId) {
        UserVo userVo = getUser(ptId);
        if (userVo != null) {
            return tokens.get(userVo.getToken());
        }
        logger.error("getSessionByPtId ==null " + ptId);
        return null;
    }

    public static UserVo getUser(IoSession session) {
        String ptId = getRoleId(session);
        UserVo um = null;
        if (ptId != null) {
            um = UserContext.getUser(ptId);
        }

        return um;
    }

    public static String getRoleId(IoSession session) {
        if (session != null) {
            if (session.getAttribute(KeyUtil.PtId) != null) {
                return (String) session.getAttribute(KeyUtil.PtId);
            }
        } else {
            logger.error("UserContext ,session is null");
        }
        return null;
    }

    public static void addUser(IoSession session, UserVo userVo) {
        userVo.setIndex(getIndex(session));
        users.put(userVo.getPtId(), userVo);
        tokens.put(userVo.getToken(), session);
        accounts.put(userVo.getAccount(), userVo.getPtId());
        names.put(userVo.getUserName(), userVo.getPtId());
    }

    public static int getIndex(IoSession session) {
        return (int) (UserContext.START_INDEX + session.getSessionId());
    }

    public static void removeUser(String ptId) {
        UserVo userVo = users.remove(ptId);
        tokens.remove(userVo.getToken());
        accounts.remove(userVo.getAccount());
        names.remove(userVo.getUserName());
    }

    public static UserVo getUser(String ptId) {
        return users.get(ptId);
    }

    public static UserVo getUserByName(String name) {
        if (names.get(name) != null) {
            return users.get(names.get(name));
        }
        return null;
    }

    public static ConcurrentMap<Integer, UserVo> getUsers() {
        return users;
    }

    public static void setUsers(ConcurrentMap<String, UserVo> users) {
        users = users;
    }


    public static void responseIntegral(IoSession session, UserVo userVo) {
        IMessage cmd = new Message(CmdType.CMD111 + "");
        ResponseData responseData = new ResponseData(CmdType.CMD111);
        Response111 response109 = new Response111();
        response109.setIntegral(userVo.getDiamond());
        responseData.setParams(response109);
        responseData.setTrue();
        responseData.setMsg("积分变更");
        cmd.setCommand(responseData.getAction() + "");
        cmd.setData(JsonUtils.objectToJson(responseData));
        session.write(cmd);
    }

    public static ConcurrentMap<String, Integer> getNames() {
        return names;
    }

    public static void setNames(ConcurrentMap<String, Integer> names) {
        UserContext.names = names;
    }
}
