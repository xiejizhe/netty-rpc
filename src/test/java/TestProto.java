import lucky.xjz.rpc.core.protocol.ProtoCodec;

import java.util.Random;

public class TestProto implements ProtoCodec {


    @Override
    public byte[] encode() throws Exception {
        byte[] data=new byte[1024*1024];
        Random random=new Random();
        random.nextBytes(data);
        return data;
    }

    @Override
    public void decode(byte[] encode) throws Exception {

    }

}
