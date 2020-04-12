package lucky.xjz.rpc.tcpserver;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * IdleStateHandler 会把监听到的读 写 读写超时异常交给下一个handler去处理
 * 只是作为使用demo 实际场景实际使用
 */
public class ServerIdleStateTrigger extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()){
                case READER_IDLE:
                    System.err.println("读超时"+ctx.channel().remoteAddress());
                    break;
                case WRITER_IDLE:
                    System.err.println("写超时"+ctx.channel().remoteAddress());
                    break;
                case ALL_IDLE:
                    System.err.println("读写超时"+ctx.channel().remoteAddress());
                    break;

            }
            ctx.channel().close();

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
