package lucky.xjz.rpc.tcpserver.handler;

import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandler;
import lucky.xjz.rpc.tcpserver.Reponse;
import lucky.xjz.rpc.tcpserver.ReponseProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lucky.xjz.rpc.core.protocol.ProtoCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ChannelHandler.Sharable
public class ReponseEncoder extends MessageToMessageEncoder<Reponse> {

    private Logger logger=LoggerFactory.getLogger("tcp-server");
    @Override
    protected void encode(ChannelHandlerContext ctx, Reponse msg, List<Object> out) throws Exception {

        System.err.println("进入到ReponseEncoder方法");
            ReponseProto.ReponseMessage.Builder reponseMessage = ReponseProto.ReponseMessage.newBuilder();

            reponseMessage.setCode(msg.getCode())
                    .setId(msg.getId())
                    .setRpcVersion(msg.getRpcVersion());

            Object obj = msg.getData();

            if (obj != null && obj instanceof ProtoCodec) {

                reponseMessage.setData(ByteString.copyFrom(((ProtoCodec) obj).encode()));
            }
            out.add(reponseMessage.build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("",cause);
        ctx.close();
    }
}
