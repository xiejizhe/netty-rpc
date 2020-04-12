package lucky.xjz.rpc.service;
import lucky.xjz.rpc.core.util.SpringUtil;
import lucky.xjz.rpc.tcpserver.Server;

public class ServerRun {


    public static void main(String[] args) {
        try {
            Server server = SpringUtil.getInstance().getBean(Server.class);
            server.start(5883);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
