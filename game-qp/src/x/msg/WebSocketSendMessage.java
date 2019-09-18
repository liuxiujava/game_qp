package x.msg;

import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import x.utils.JsonUtils;

public class WebSocketSendMessage {

    public static void writeAndFlush(NioSocketChannel ctx, ResponseData responseData) {
        TextWebSocketFrame tws = new TextWebSocketFrame(JsonUtils.objectToJson(responseData));
        ctx.writeAndFlush(tws);
    }
}
