package lucky.xjz.rpc;
import com.google.common.collect.Queues;
import io.netty.channel.Channel;
import lucky.xjz.rpc.tcpserver.Reponse;
import lucky.xjz.rpc.tcpclient.Request;
import lucky.xjz.rpc.core.util.ThreadPollUtil;
import lucky.xjz.rpc.tcpclient.ServerChannelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MessageQueue {

    private final Logger log = LoggerFactory.getLogger("MessageQueue");

    private Queue<Request> requestQueue = Queues.newLinkedBlockingQueue();

    private Queue<Reponse> reponseQueues = Queues.newLinkedBlockingQueue();

    private ScheduledExecutorService timer = null;

    @Autowired
    private ServerChannelManager serverChannelManager;

    private MessageQueue() {

        timer = ThreadPollUtil.getScheduledExecutor(4);
        active();
    }


    public void addRequest(Request request) {
        if (request != null) {
            requestQueue.add(request);
        }

    }

    public void addReponse(Reponse reponse) {

        if (reponse != null) {
            reponseQueues.add(reponse);
        }

    }

    private void sendRequest(Request request){

        if(request!=null){
            Channel channel=serverChannelManager.isExist(request.getSocketAddress());
            if(channel!=null){
                log.info("请求节点 "+request.getSocketAddress()+" 请求Id "+request.getId()+" 请求指令 "+request.getMethod());
                channel.writeAndFlush(request);
            }
        }
    }

    private void sendReponse(Reponse reponse){



    }

    private void active() {
        log.info("-----消息队列已开启-----");
        timer.scheduleAtFixedRate(() -> {

            try {
                Request request=requestQueue.poll();
                sendRequest(request);

                Reponse reponse=reponseQueues.poll();
                sendReponse(reponse);

            } catch (Exception e) {
                log.info("", e);
            }

        }, 30, 30, TimeUnit.MILLISECONDS);


    }

    public ServerChannelManager getServerChannelManager() {
        return serverChannelManager;
    }

}
