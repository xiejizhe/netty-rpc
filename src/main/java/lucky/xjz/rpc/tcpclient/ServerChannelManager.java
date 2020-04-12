package lucky.xjz.rpc.tcpclient;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ServerChannelManager {

    private ServerChannelManager(){}
    private Map<SocketAddress, Channel> serverChannel= new ConcurrentHashMap<>();

    public Channel isExist(SocketAddress socketAddress) {

        return serverChannel.get(socketAddress);
    }

    public void addChannel(SocketAddress socketAddress,Channel channel){
        serverChannel.put(socketAddress,channel);
    }

    public boolean removeChannel(SocketAddress socketAddress){

         if(serverChannel.remove(socketAddress)!=null){
             return true;
         }
         return false;

    }

}
