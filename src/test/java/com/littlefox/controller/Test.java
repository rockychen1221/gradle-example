package com.littlefox.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    static int numb=0;

    static class MultiThread extends Thread {

        private List<Integer> idList;

        private CountDownLatch countDownLatch;

        public void setIdList(List<Integer> idList) {
            this.idList = idList;
        }

        public void setCountDownLatch(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                //Thread.sleep(1000);
                numb++;
                for (int i=0;i<this.idList.size();i++){
                    System.out.println(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();    //获取开始时间

        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            idList.add(i);
        }
        int threadNum = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            MultiThread thread = new MultiThread();
            thread.setIdList(idList);
            thread.setCountDownLatch(countDownLatch);
            executorService.submit(thread);
        }
        countDownLatch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();    //获取结束时间

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

    }
}
