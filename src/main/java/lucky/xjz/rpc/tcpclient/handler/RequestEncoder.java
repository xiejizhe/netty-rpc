package lucky.xjz.rpc.tcpclient.handler;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandler;
import lucky.xjz.rpc.tcpclient.Request;
import lucky.xjz.rpc.tcpclient.RequestProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ChannelHandler.Sharable
public class RequestEncoder extends MessageToMessageEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, List<Object> out) throws Exception {
        try {
            List<ByteString> params = new ArrayList<>();
            msg.getParams().forEach((v) -> {

                params.add(ByteString.copyFrom(v));

            });
            RequestProto.RequestMessage requestMessage = RequestProto.RequestMessage.newBuilder()
                    .setId(msg.getId())
                    .setMethodName(msg.getMethod())
                    .setRpcVersion(msg.getRpcVersion())
                    .addAllParams(params)
                    .build();

            out.add(requestMessage);
        } catch (Exception e) {
            throw new Exception(e);
        }

    }


}
