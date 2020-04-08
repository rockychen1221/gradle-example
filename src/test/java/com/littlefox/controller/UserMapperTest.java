package com.littlefox.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.littlefox.example.dao.UserMapper;
import com.littlefox.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    private String str="5C070830295AD870F3DC867AB24CA22B"; //AES

    private Long startTime=0l, endTime=0l;

    @Resource
    private UserMapper userMapper;

    static class MyThread implements Runnable {
        private List<String> list;
        private CountDownLatch begin;
        private CountDownLatch end;

        //创建个构造函数初始化 list,和其他用到的参数
        public MyThread(List<String> list, CountDownLatch begin, CountDownLatch end) {
            this.list = list;
            this.begin = begin;
            this.end = end;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < list.size(); i++) {
                    //这里还要说一下，，由于在实质项目中，当处理的数据存在等待超时和出错会使线程一直处于等待状态    //这里只是处理简单的，    //分批 批量插入
                }
                //执行完让线程直接进入等待
                begin.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                //这里要主要了，当一个线程执行完 了计数要减一不然这个线程会被一直挂起
                // ，end.countDown()，这个方法就是直接把计数器减一的
                end.countDown();
            }
        }
    }

    public static void exec(List<String> list) throws InterruptedException {
        int count = 300;                   //一个线程处理300条数据
        int listSize = list.size();        //数据集合大小
        int runSize = (listSize / count) + 1;  //开启的线程数
        List<String> newlist = null;       //存放每个线程的执行数据
        ExecutorService executor = Executors.newFixedThreadPool(runSize);      //创建一个线程池，数量和开启线程的数量一样
        //创建两个个计数器
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(runSize);
        //循环创建线程
        for (int i = 0; i < runSize; i++) {
            //计算每个线程执行的数据
            if ((i + 1) == runSize) {
                int startIndex = (i * count);
                int endIndex = list.size();
                newlist = list.subList(startIndex, endIndex);
            } else {
                int startIndex = (i * count);
                int endIndex = (i + 1) * count;
                newlist = list.subList(startIndex, endIndex);
            }
            //线程类
            MyThread mythead = new MyThread(newlist, begin, end);
            //这里执行线程的方式是调用线程池里的executor.execute(mythead)方法。
            executor.execute(mythead);
        }
        begin.countDown();
        end.await();
        //执行完关闭线程池
        executor.shutdown();
    }

    int numb=0;

    class MultiThread extends Thread {
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
                Thread.sleep(1000);
                System.out.println("-=-=-="+this.idList);

                numb++;

                for (int i=0;i<this.idList.size();i++){
                    User user = User.builder()
                            .userName("userName" + i).phone("phone" + i)
                            .build();
                    userMapper.insert(user);
                }

                System.out.println("countDownLatch"+countDownLatch);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        }
    }

    @Before
    public void start(){
        startTime = System.currentTimeMillis();    //获取开始时间
    }

    @After
    public void end(){
        endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }

    @Test
    public void insert() throws InterruptedException {
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            idList.add(i);
        }
        int threadNum = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        int perSize = idList.size() / threadNum;
        for (int i = 0; i < threadNum; i++) {
            MultiThread thread = new MultiThread();
            thread.setIdList(idList);
            //thread.setIdList(idList.subList(i * perSize, (i + 1) * perSize));
            thread.setCountDownLatch(countDownLatch);
            executorService.submit(thread);
        }
        countDownLatch.await();
        executorService.shutdown();

    }

    @Test
    public void queryList() {
        //List<User> users=userService.queryList(new HashMap<>());
        //users.forEach(user -> System.out.println(user.toString()));
        List<User> users2 = userMapper.queryUserList(User.builder().build());
        System.out.println(users2.size());
        users2.forEach(user2 -> System.out.println(user2.toString()));
    }

    @Test
    public void queryUserList() {
        //List<User> users2 = userService.queryUserList(User.builder().id("F98B1ACAAE38C2C512E14034F82C6993").build());
        List<User> users = userMapper.queryUserList(User.builder().build());
        System.out.println("查询总数量:"+users.size());
    }

    /**
     * 测试pagehelper分页
     */
    @Test
    public void queryPageList() {
        //分页
        PageHelper.startPage(0,10,true);
        //查询数据
        List<User> list = userMapper.queryUserList(User.builder().build());
        //返回分页后集合
        PageInfo<User> users2 = new PageInfo<User>(list);
        users2.getList().forEach(user2 -> System.out.println(user2.toString()));
    }

    /**
     * by ID 查询单个信息（包含对象本身和继承类）
     */
    @Test
    public void queryById() {
        //获取开始时间
        User user=userMapper.queryById(User.builder().id("5C070830295AD870F3DC867AB24CA22B").build());
        System.out.println(user.toString());
    }

    /**
     * by ID
     */
    @Test
    public void query() {
        //获取开始时间
        User user=userMapper.query("5C070830295AD870F3DC867AB24CA22B");
        System.out.println(user.toString());
    }


    /**
     * by ID 获取用户角色
     */
    @Test
    public void queryUserRole() {
        //获取开始时间
        User user=userMapper.queryUserRole("5C070830295AD870F3DC867AB24CA22B");
        System.out.println(user.toString());
    }

    /**
     * by ID 修改
     */
    @Test
    public void update() {
        User user=User.builder().id("5C070830295AD870F3DC867AB24CA22B").phone("17621210966").userName("userName.rockychen").build();
        user.setAge("23");
        user.setAddress("China.ShangHai");
        userMapper.update(user);
    }

    /**
     * by ID 删除
     */
    @Test
    public void delete() {
        userMapper.delete(User.builder().userName("userName5").build());
    }

}
