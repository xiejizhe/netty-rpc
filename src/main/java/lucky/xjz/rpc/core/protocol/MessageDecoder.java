package lucky.xjz.rpc.core.protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import static lucky.xjz.rpc.core.protocol.ProtocolConstant.HEADER;

public class MessageDecoder extends ByteToMessageDecoder {

    private final int MAX_DATA_LENGTH = 1024 * 1024*2;

    private final String MSG_HEADER = HEADER.getConstant();
    //一个int 占有四字节长度
    private final int INT_BYTE_LENGTH = 4;

    private final Logger log = LoggerFactory.getLogger("Tcp-Client");

    /**
     *
     * @param ctx
     * @param in         接收到的数据
     * @param out        将数据交给下一个handler 处理的数据载体
     * @throws Exception 将异常交给下一个handler处理
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        while (true) {

            //读取消息长度
            int readableLen = in.readableBytes();
            //是否满足消息最小长度
            if(readableLen<=0){
                break;
            }
            if (readableLen < MSG_HEADER.length() + INT_BYTE_LENGTH) {
                log.info("msg length is too short");
                break;
            }
            //判断消息是否大于MAX_DATA_LENGTH
            if (readableLen >= MAX_DATA_LENGTH) {
                //忽略所有可读的字节
                in.skipBytes(readableLen);
                log.info(ctx.channel().remoteAddress()+" The data sent is too large, which is regarded as socket attack");
                return;
            }

            //读取消息头 填充到字节数组
            byte[] header = new byte[MSG_HEADER.length()];
            //标记此时读取的位置
            in.markReaderIndex();
            //读取消息头
            in.readBytes(header);
            //判断消息类型(应该改成字节判断)
            if (MSG_HEADER.equals(new String(header))) {

                //获取数据长度
                int length = in.readInt();
                //获取可读字节数
                int readAbleLength = in.readableBytes();
                //可读字节数小于数据真实长度  数据损坏 回到标记位置 等待后续数据到来
                if (readAbleLength < length) {

                    in.resetReaderIndex();
                    //log.info("解码器可读字节数小于数据真实长度");
                    break;
                }
                //反序列化字节数组
                byte[] data = new byte[length];
                in.readBytes(data);

                ByteBuf byteBuf = Unpooled.copiedBuffer(data);

                //将数据交给下一个handler处理
                out.add(byteBuf);

            }
//            else {
//                //throw new Exception("The message protocol "+new String(header,"gbk")+" sent by the client is incorrect");
//            }

        }

    }
}
