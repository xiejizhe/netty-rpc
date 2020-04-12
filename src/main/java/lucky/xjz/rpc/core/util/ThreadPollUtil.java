package lucky.xjz.rpc.core.util;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.*;

public class ThreadPollUtil {
    /**
     * 这里面的参数实际开发中应该放在单独的配置文件中进行维护
     */
    private ThreadPollUtil() {

    }

    private static ExecutorService executorService = null;

    public static ScheduledExecutorService getScheduledExecutor(int coreSize) {

        return Executors.newScheduledThreadPool(coreSize);

    }

    public static ExecutorService getExecutor() {
        if(executorService==null){
            executorService=getExecutor(32, 128, 60, 65536);
            return executorService;
        }
        return executorService;
    }


    private static ExecutorService getExecutor(int corePollSize, int maxPoolSize, int keepAliveTime, int blockQueueCapacity) {

        return new ThreadPoolExecutor(
                corePollSize, maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(blockQueueCapacity),
                //new ThreadPoolExecutor.AbortPolicy()
                new ThreadFactoryBuilder().setNameFormat("clientNum-pool-%d").build());
    }

    public static void close(ExecutorService executor) {

        executor.shutdown();

    }

}
