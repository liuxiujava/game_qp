package x.game;

import jsa.log.Logger;
import jsa.log.LoggerFactory;
import x.executor.ExecutorDispatcher;
import jsa.utils.RandomUtil;
import x.context.NoticeContext;
import x.context.RoomContext;
import x.vo.UserVo;

import java.util.*;

public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class, ExecutorDispatcher.LogActor);

    protected String ptId;
    protected String userName;
    private String head;
    protected int pos;  //顺序位置
    protected int online = 1; //是否在线（在房间）  1：在房间   2：已离开

    protected RoomInfo roomInfo;

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }
}
