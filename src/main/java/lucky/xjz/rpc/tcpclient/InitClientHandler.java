package lucky.xjz.rpc.tcpclient;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lucky.xjz.rpc.tcpserver.ReponseProto;
import lucky.xjz.rpc.core.protocol.MessageDecoder;
import lucky.xjz.rpc.core.protocol.ProtocolWrapper;
import lucky.xjz.rpc.tcpclient.handler.ClientHandler;
import lucky.xjz.rpc.tcpclient.handler.RequestEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitClientHandler{
    @Autowired
    private RequestEncoder requestEncoder;
    @Autowired
    private ClientHandler clientHandler;
    private final Logger log = LoggerFactory.getLogger("tcp-Client");
    protected void initChannel(SocketChannel ch){
       try {
           ChannelPipeline cp=ch.pipeline();
           cp.addLast("MessageDecoder", new MessageDecoder());
           cp.addLast("ProtobufVarint32FrameDecoder", new ProtobufVarint32FrameDecoder());
           cp.addLast("ProtobufDecoder", new ProtobufDecoder(ReponseProto.ReponseMessage.getDefaultInstance()));
           cp.addLast("ProtocolWrapper", new ProtocolWrapper());
           cp.addLast("ProtobufVarint32LengthFieldPrepender", new ProtobufVarint32LengthFieldPrepender());
           cp.addLast("ProtobufEncoder", new ProtobufEncoder());
           cp.addLast("RequestEncoder",requestEncoder );
           cp.addLast("ClientServiceHandler", clientHandler);
       }catch (Exception e){
           log.info("initChannel 初始化失败",e);
       }


    }
}
