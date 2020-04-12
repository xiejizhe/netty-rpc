package lucky.xjz.rpc.tcpserver.handler;

import io.netty.channel.ChannelHandler;
import lucky.xjz.rpc.tcpclient.RequestProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ChannelHandler.Sharable
public class ServerHandelr extends SimpleChannelInboundHandler<RequestProto.RequestMessage> {

    private final Logger log = LoggerFactory.getLogger("Tcp-Server");
    public ServerHandelr() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestProto.RequestMessage msg) throws Exception {
        try {
            System.err.println("请求Id:" + msg.getId());
        } catch (Exception e) {
            log.info("",e);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("ServerHandelr exceptionCaught" + ctx.channel().remoteAddress() + "连接", cause);
        ctx.close();

    }

}
