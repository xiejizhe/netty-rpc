package lucky.xjz.rpc.tcpserver;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lucky.xjz.rpc.tcpclient.RequestProto;
import lucky.xjz.rpc.core.protocol.MessageDecoder;
import lucky.xjz.rpc.core.protocol.ProtocolWrapper;
import lucky.xjz.rpc.tcpserver.handler.ReponseEncoder;
import lucky.xjz.rpc.tcpserver.handler.ServerHandelr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitServerHandler {

    @Autowired
    private ServerHandelr serverHandelr;
    @Autowired
    private ReponseEncoder reponseEncoder;

    private final Logger log= LoggerFactory.getLogger("Tcp-server");
    protected void initChannel(SocketChannel ch) {
        try {
            ChannelPipeline cp=ch.pipeline();
            cp.addLast("MessageDecoder", new MessageDecoder());
            cp.addLast("ProtobufVarint32FrameDecoder", new ProtobufVarint32FrameDecoder());
            cp.addLast("ProtobufDecoder", new ProtobufDecoder(RequestProto.RequestMessage.getDefaultInstance()));
            cp.addLast("ProtocolWrapper", new ProtocolWrapper());
            cp.addLast("ProtobufVarint32LengthFieldPrepender", new ProtobufVarint32LengthFieldPrepender());
            cp.addLast("ProtobufEncoder", new ProtobufEncoder());
            cp.addLast("ReponseEncoder", reponseEncoder);
            cp.addLast("ServerServiceHandler", serverHandelr);
        }catch (Exception e){
            log.info("initChannel 失败",e);
        }

    }
}
