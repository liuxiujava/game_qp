package x.frame.listerner;

import jsa.event.EventContainer;
import jsa.event.EventListener;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import ws.nett.message.IMessage;
import ws.nett.message.Message;
import x.context.RoomContext;
import x.executor.ExecutorDispatcher;
import x.frame.event.FrameEvent;
import x.frame.event.RoomEvent;
import x.game.RoomInfo;
import x.utils.CmdType;

public class RoomSyncListener implements EventListener<FrameEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RoomSyncListener.class, ExecutorDispatcher.LogActor);


    @Override
    public void onEvent(FrameEvent frameEvent) throws Exception {
        RoomInfo roomInfo = frameEvent.getRoomInfo();
        roomInfo.addFrameIndex();
        if (roomInfo.getFrameIndex() == 8) {
            String aa = "";
            for (IPlayer iPlayer : roomInfo.getUsers()) {
                aa += "//id=" + iPlayer.getPtId() + "ready=" + iPlayer.getIsReady() + "pos=" + iPlayer.getPos() + "//";
            }
            logger.error(roomInfo.getRoomId() + " Sync  剩余格子=" + roomInfo.getQizi().size() + " UserSize=" + roomInfo.getUsers().size() + "_roomStatus_" + roomInfo.getStatus() + " 剩余次数=" + (roomInfo.getGameNum() - roomInfo.getThisNum()) + "__" + aa);
            roomInfo.setFrameIndex(0);
        }

        if (roomInfo.getStatus() == 3) {
            if (System.currentTimeMillis() >= roomInfo.getChuziTime()) {
                for (IPlayer iPlayer : roomInfo.getUsers()) {
                    if (iPlayer instanceof RobotUser) {
                        continue;
                    }
                    if (iPlayer.getIsReady() == 2) {
                        IMessage cmd = new Message(CmdType.CMD113 + "");
                        RoomEvent event = new RoomEvent(RoomEvent.UserReady, null);
                        event.setRoomInfo(roomInfo);
                        event.setPtId(iPlayer.getPtId());
                        event.setMessage(cmd);
                        EventContainer.dispatchEvent(event);
                    }
                }
            }
        } else if (roomInfo.getStatus() == 1) {
            if (roomInfo.getFrameIndex() % 4 == 0) {
                logger.error(roomInfo.getRoomId() + " ptId =" + roomInfo.getChuziPtId() + " 下次CD 剩余 " + (roomInfo.getChuziTime() - System.currentTimeMillis()));
            }
            if (System.currentTimeMillis() >= roomInfo.getChuziTime()) {
                IPlayer user = roomInfo.getUser(roomInfo.getChuziPtId());
                long time1 = System.currentTimeMillis();
                //判定违规
                user.run(null);
                logger.error(roomInfo.getRoomId() + " ptId=" + user.getPtId() + "下子成功耗时毫秒 time " + (System.currentTimeMillis() - time1));
            }

            if (roomInfo.getQhType() != 0 && roomInfo.getQhTime() != 0) {
                if ((System.currentTimeMillis() - roomInfo.getQhTime()) > 60 * 1000) {
                    roomInfo.updateQhMessage(2, "");
                }
            }

        } else {
            if (roomInfo.getType() != 0) {
                if (roomInfo.haveJoiin() && System.currentTimeMillis() >= roomInfo.getPiPeiTime()) {
                    IPlayer iPlayer = roomInfo.addRobot();
                    RoomContext.joinAndExitRoom(roomInfo, iPlayer, 0);

                    roomInfo.isReady(iPlayer.getPtId());
                }
            }

        }
    }
}
