package lucky.xjz.rpc.tcpclient.handler;

import io.netty.channel.ChannelHandler;
import lucky.xjz.rpc.tcpclient.SendMessage;
import lucky.xjz.rpc.tcpserver.ReponseProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lucky.xjz.rpc.tcpclient.ServerChannelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

@Component
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ReponseProto.ReponseMessage> {

    private static final Logger log = LoggerFactory.getLogger("tcp-client");
    @Autowired(required = true)
    private ServerChannelManager serverChannelManager;
    @Autowired
    private SendMessage sendMessage;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ReponseProto.ReponseMessage msg) throws Exception {

        try {
            //业务代码 耗时业务放进线程池执行
            System.err.println(msg.getCode());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        log.info("ClientHandler    channelActive " + ctx.channel().remoteAddress() + "通道可用");

    }

    /**
     * 当远程服务器挂掉以后 客户端队列中未发送的消息需要执行
     * 避免服务端挂点以后客户端不能感知消息的发送情况
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SocketAddress socketAddress=ctx.channel().remoteAddress();
        serverChannelManager.removeChannel(socketAddress);
        sendMessage.executeCallBackAndRemove(socketAddress);
        log.info("ClientHandler" + socketAddress+" is ChannelInActive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        log.info("ClientHandler   exceptionCaught " + ctx.channel().remoteAddress(), cause);
    }


}
