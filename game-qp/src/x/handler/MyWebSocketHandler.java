package x.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import jsa.dispatcher.IDispatcher;
import jsa.log.LoggerFactory;
import ws.nett.handler.WebSocketHandler;
import ws.nett.message.IMessage;
import ws.nett.message.Message;
import ws.nett.session.IoSession;
import ws.nett.session.SessionManager;
import x.context.UserContext;
import x.executor.ExecutorDispatcher;
import x.utils.KeyUtil;

/**
 * ClassName:MyWebSocketServerHandler Function: TODO ADD FUNCTION.
 *
 * @author hxy
 */
public class MyWebSocketHandler extends WebSocketHandler {
    final static jsa.log.Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class, ExecutorDispatcher.LogActor);

    /**
     * channel 通道 action 活跃的 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.error("连接O开启：" + ctx.channel().remoteAddress().toString());
    }

    /**
     * channel 通道 Inactive 不活跃的 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        IoSession session = SessionManager.getSessionByChannelId(ctx.channel().id().toString());
        if (session != null) {
            logger.error(" session.close 11111111111");
            session.close(true);
        }
    }

    private void doExit(IoSession session) {
        IMessage msg = new Message("u_e");
        try {
            messageReceived(session, msg);
        } catch (Exception e) {
            logger.error("doExit execute error" + session.getSessionId() + "_" + e);
        }
    }

    /**
     * 接收客户端发送的消息 channel 通道 Read 读 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
            // WebSocket接入
        } else if (msg instanceof WebSocketFrame) {
            IoSession session = SessionManager.getSessionByChannelId(ctx.channel().id().toString());
            logger.error(handshaker.uri());
            handlerWebSocketFrame2(session, (WebSocketFrame) msg);
        }
    }

    /**
     * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * exception 异常 Caught 抓住 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public IDispatcher getDispatcher(IoSession session, IMessage message) {
        return ExecutorDispatcher.matchDispatcher(session, message);
    }

    @Override
    public void sessionCreated(IoSession var1) {

    }

    @Override
    public void sessionClosed(IoSession session) {
        logger.error("连接X关闭：" + session.getChannel().remoteAddress().toString());
        Object old = session.getAttribute(KeyUtil.OldSession);
        if (old != null) {
            logger.error("userExited sessionId" + session.getSessionId() + " old != null");
            return;
        } else {
            doExit(session);
        }
    }

    @Override
    public void exceptionCaught(IoSession var1, IMessage var2, Throwable var3) {

    }

    @Override
    public void readException(IoSession var1) {

    }

    @Override
    public void writeException(IoSession var1) {

    }


    @Override
    public void messageReceived(IoSession session, IMessage msg) {
        execute(session, msg);
    }

    @Override
    public void messageSent(IoSession var1, IMessage var2) {

//        TextWebSocketFrame tws = new TextWebSocketFrame(JsonUtils.objectToJson(var2.get()));
//        channel.writeAndFlush(tws);
    }

    @Override
    public void sessionIdle(IoSession var1) {

    }
}