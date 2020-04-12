package lucky.xjz.rpc.core.protocol;

public interface ProtoCodec {

    byte[] encode() throws Exception;

    void decode(byte[] encode) throws  Exception;
}
