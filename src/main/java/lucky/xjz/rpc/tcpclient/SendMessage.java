package lucky.xjz.rpc.tcpclient;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import io.netty.channel.Channel;
import lucky.xjz.rpc.MessageQueue;
import lucky.xjz.rpc.core.CallBack;
import lucky.xjz.rpc.core.TaskCallBack;
import lucky.xjz.rpc.core.util.ThreadPollUtil;
import lucky.xjz.rpc.core.util.MakeReponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

@Component
public class SendMessage {

    private SendMessage() {
    }

    private final Logger log = LoggerFactory.getLogger("Tcp-client");

    @Autowired
    private Client client;
    @Autowired
    private MessageQueue messageQueue;

    private Map<String, CallBack> callBackMap = Maps.newConcurrentMap();

    private BlockingQueue<TaskCallBack> syncSendMessageTaskQueue = Queues.newLinkedBlockingQueue(1);

    public void sendRequest(Request request, CallBack callBack) throws Exception {

        SocketAddress socketAddress = request.getSocketAddress();
        //添加回调
        callBackMap.put(request.getId(), callBack);
        //如果队列容量已满 会阻塞 直到等待队列有容量
        syncSendMessageTaskQueue.put(() -> {
            //判断是否存在通道
            if (null != (isExistChannel(socketAddress))) {
                log.info("exist" + socketAddress + "channel");
                messageQueue.addRequest(request);
                //清空队列
                syncSendMessageTaskQueue.clear();
            } else {
                log.info("is not exist" + socketAddress + "channel");
                CountDownLatch countDownLatch = new CountDownLatch(1);
                connectServer(request, socketAddress, countDownLatch);
                try {
                    countDownLatch.await();
                    //存在就说明握手成功
                    if (isExistChannel(socketAddress) != null) {
                        messageQueue.addRequest(request);
                    }
                } catch (InterruptedException e) {
                    log.error("",e);
                } finally {
                    //清空队列
                    syncSendMessageTaskQueue.clear();
                }
            }

        });
        syncSendMessageTaskQueue.peek().execute();
    }

    public Channel isExistChannel(SocketAddress socketAddress) {

        return messageQueue.getServerChannelManager().isExist(socketAddress);

    }

    public void connectServer(Request request, SocketAddress socketAddress, CountDownLatch countDownLatch) {

        ThreadPollUtil.getExecutor().execute(() -> {
            try {
                client.connect(socketAddress, countDownLatch);
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    log.error("connect " + socketAddress + " is fail,reason: ", e.getMessage());
                } else if(e instanceof ConnectException) {
                    log.info("connect "+socketAddress+" timeout please: " ,e.getMessage());
                }else{
                    log.error("countDownLatch is can't exceed 1 ",e.getMessage());
                }
                executeCallBack(request);
                countDownLatch.countDown();
            }
        });

    }

    public void executeCallBack(Request request) {
        if (callBackMap.containsKey(request.getId())) {
            CallBack callBack = callBackMap.get(request.getId());

            if (callBack != null) {
                callBack.execute(MakeReponse.getConnectTimeOutReponse(request));
                callBackMap.remove(request.getId());
            }
        }

    }

    public void executeCallBackAndRemove(SocketAddress address) {

        callBackMap.forEach((k, v) -> {

            if (k.startsWith(address + "")) {
                String id = k.substring(address.toString().length());
                v.execute(MakeReponse.getConnectTimeOutReponse(id));
                callBackMap.remove(k);
            }
        });

    }

}
