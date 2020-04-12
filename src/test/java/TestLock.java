import com.google.common.collect.Queues;

import java.util.concurrent.BlockingQueue;

public class TestLock {


    public synchronized void test() throws InterruptedException {

        Thread.sleep(10000);
        System.err.println("111111111");
    }


    private static BlockingQueue<String> blockingQueue = Queues.newSynchronousQueue();


    public static void main(String[] args) {


//        new Thread(() -> {
//
//            try {
//                blockingQueue.put("1111111");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }).start();

        new Thread(() -> {

            try {
                System.err.println(blockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

    }

}
