import lucky.xjz.rpc.tcpclient.Request;
import lucky.xjz.rpc.core.util.SpringUtil;
import lucky.xjz.rpc.tcpclient.SendMessage;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        SendMessage sendMessage = SpringUtil.getInstance().getBean(SendMessage.class);
        SocketAddress socketAddress = new InetSocketAddress("192.168.31.60", 5883);
        System.err.println("sendMessage--->" + sendMessage);


        for (int i = 0; i < 10; i++) {
            new Thread(() -> {

                try {
                    TestProto testProto = new TestProto();
                    List<byte[]> listProto = new ArrayList<>();
                    listProto.add(testProto.encode());
                    sendMessage.sendRequest(new Request(socketAddress, "test111111111111", listProto), (obj) -> {

                        System.err.println(obj);

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

        }

    }
}
