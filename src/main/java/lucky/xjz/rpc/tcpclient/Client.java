package lucky.xjz.rpc.tcpclient;
import lucky.xjz.rpc.core.protocol.HandShakeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;

@Component
public class Client {

    @Autowired
    private ServerChannelManager serverChannelManager;
    @Autowired
    private InitClientHandler initClientHandler;
    private final Logger log = LoggerFactory.getLogger("Tcp-Client");
    //身份
    private final boolean role = false;

    private EventLoopGroup worker;
    private Client() {

        worker = new NioEventLoopGroup();

    }

    public void connect(SocketAddress address, CountDownLatch countDownLatch) throws Exception {

        if (countDownLatch.getCount() > 1) {

            throw new Exception("countDownLatch of size is than one ");
        }
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(worker)
                    .option(ChannelOption.SO_KEEPALIVE, true)//长连接
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline cp = ch.pipeline();
                            cp.addLast("HandShakeHandler", new HandShakeHandler(role, (obj) -> {

                                if (obj instanceof Channel) {
                                    Channel channel = (Channel) obj;
                                    serverChannelManager.addChannel(channel.remoteAddress(), channel);
                                    initClientHandler.initChannel(ch);
                                }
                                countDownLatch.countDown();
                            }));
                        }
                    });
            ChannelFuture future = bootstrap.connect(address).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){

            log.info("connect "+address+"occured Exception!");
        }
        finally {
            worker.shutdownGracefully();
        }
    }


}
