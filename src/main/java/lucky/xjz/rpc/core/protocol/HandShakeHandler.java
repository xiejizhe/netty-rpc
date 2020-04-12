package lucky.xjz.rpc.core.protocol;
import lucky.xjz.rpc.core.CallBack;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import static lucky.xjz.rpc.core.protocol.ProtocolConstant.CLIENT_HELLO;
import static lucky.xjz.rpc.core.protocol.ProtocolConstant.SERVER_HELLO;

/**
 * 握手handler 握手成功以后删除
 */
public class HandShakeHandler extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger("core");
    private CallBack callBack;
    private boolean role;

    public HandShakeHandler(boolean role, CallBack callBack) throws Exception {

        if (callBack == null) {
            throw new Exception("error callback is null");
        }
        this.callBack = callBack;
        this.role = role;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();

        if (length == 0) {
            return;
        }
        byte[] data = new byte[length];

        in.readBytes(data);

        if (CLIENT_HELLO.getConstant().equals(new String(data, "UTF-8"))) {

            //如果是client 发过来的消息 则回复一个helloMessage
            log.info("Client request handshake received");
            ByteBuf byteBuf = Unpooled.copiedBuffer(SERVER_HELLO.getConstant().getBytes("UTF-8"));
            ctx.writeAndFlush(byteBuf);
            ctx.pipeline().remove("HandShakeHandler");
            callBack.execute(ctx.channel());

        } else if (SERVER_HELLO.getConstant().equals(new String(data, "UTF-8"))) {
            log.info("Receive the information of server response handshake");
            ctx.pipeline().remove("HandShakeHandler");
            callBack.execute(ctx.channel());

        }

        return;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //向远程节点发送握手信息
        if (!role) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(CLIENT_HELLO.getConstant().getBytes("UTF-8"));
            log.info("Initialize successfully, send handshake request to server");
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("handshake fail with "+ctx.channel().remoteAddress(),cause);
        ctx.close();
    }
}
