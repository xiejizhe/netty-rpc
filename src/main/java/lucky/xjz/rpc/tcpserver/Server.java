package lucky.xjz.rpc.tcpserver;
import lucky.xjz.rpc.MessageQueue;
import lucky.xjz.rpc.core.protocol.HandShakeHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class Server {

    private final Logger log = LoggerFactory.getLogger("tcp");
    private final boolean role = true;
    @Autowired
    private MessageQueue messageQueue;
    @Autowired
    private InitServerHandler initServerHandler;

    private Server() {
    }

    public void start(int port) throws Exception {

        //用于接受客户端连接的
        EventLoopGroup boss = new NioEventLoopGroup(1);
        //用于处理网络通信的(读写)
        EventLoopGroup worker = new NioEventLoopGroup();

        //用于配置通信通道的辅助类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(boss, worker)//指定两个线程组
                    .channel(NioServerSocketChannel.class)//Nio模式
                    .option(ChannelOption.SO_BACKLOG, 1024)//设置tcp缓冲区
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)//设置发送缓冲区
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)//设置接收缓冲区
                    .option(ChannelOption.SO_KEEPALIVE, true)//长连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //向通道中添加hanler(编解码 业务)
                            ChannelPipeline cp = ch.pipeline();
                            cp.addLast(new ReadTimeoutHandler(600, TimeUnit.SECONDS));
                            cp.addLast("HandShakeHandler", new HandShakeHandler(role, (obj) -> {
                            initServerHandler.initChannel(ch);
                            }));

                        }
                    });

            ChannelFuture future = serverBootstrap.bind(port).sync();//绑定端口
            future.channel().closeFuture().sync();//等待关闭
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            //释放资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

}
