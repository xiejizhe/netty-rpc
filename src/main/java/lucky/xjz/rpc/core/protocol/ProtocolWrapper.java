package lucky.xjz.rpc.core.protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import static lucky.xjz.rpc.core.protocol.ProtocolConstant.HEADER;

public class ProtocolWrapper extends MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        //初始化byte数组长度 指定数据长度
        byte[] protoData = new byte[msg.readableBytes()];
        //填充数组
        msg.readBytes(protoData);
        //bytebuf初始化 protobuf标识长度 数据长度 int的长度
        ByteBuf newData = Unpooled.buffer(HEADER.getConstant().length() + protoData.length + 4);
        //填充proto标识
        newData.writeBytes(HEADER.getConstant().getBytes());
        //填充数据长度
        newData.writeInt(protoData.length);
        //填充数据
        newData.writeBytes(protoData);
        //将数据交给下一个handler
        out.add(newData);
    }

}

