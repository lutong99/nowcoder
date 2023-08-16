package org.example.nowcoder.api;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTests {

    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(blockingQueue)).start();
        new Consumer(blockingQueue).start();
        new Consumer(blockingQueue).start();
        new Consumer(blockingQueue).start();
        new Consumer(blockingQueue).start();
        new Consumer(blockingQueue).start();


    }

    @Test
    public void test1() {Map<String, Object> map = new HashMap<>();
        map.put("zhangsan", 1);
        map.put("lisi", 2);
        map.put("wangwu", 3);
        map.put("zhaoliu", 4);
        map.put("liqi", 5);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("关羽", 0);
        map2.put("赵云", 1);
        map2.put("张飞", 32);
        map.putAll(map2);
        System.out.println("map = " + map);
        System.out.println("map2 = " + map2);

    }

}

class Producer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                queue.put(i);
                Thread.sleep(20);
                System.out.println(Thread.currentThread().getName() + "生产: " + i + ", size = " + queue.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class Consumer extends Thread {

    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer take = queue.take();
                System.out.println(Thread.currentThread().getName() + "消费: " + take + ", size = " + queue.size());
                Thread.sleep(new Random().nextInt(1000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
