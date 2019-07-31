package com.littlefox;

import com.littlefox.example.model.User;
import com.littlefox.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
@MapperScan("com.littlefox.example.**.dao")
public class Application {

	static int numb=0;

	static class MultiThread extends Thread {

		private UserService userService;

		private List<Integer> idList;

		private CountDownLatch countDownLatch;

		public void setIdList(List<Integer> idList) {
			this.idList = idList;
		}

		public void setCountDownLatch(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}


		public UserService getUserService() {
			return userService;
		}

		public void setUserService(UserService userService) {
			this.userService = userService;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				numb++;
				for (int i=0;i<this.idList.size();i++){
					User user = User.builder()
							.userName("userName" + i).phone("phone" + i)
							.build();

					user.setLid("lid"+i);
					user.setAddress("address"+i);
					user.setAge(""+i);
					userService.insert(user);
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
		ApplicationContext ctx =SpringApplication.run(Application.class, args);

		UserService userService =(UserService) ctx.getBean("userServiceImpl");

		long startTime = System.currentTimeMillis();    //获取开始时间

		List<Integer> idList = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			idList.add(i);
		}
		int threadNum = 1;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		int perSize = idList.size() / threadNum;
		for (int i = 0; i < threadNum; i++) {
			MultiThread thread = new MultiThread();
			thread.setUserService(userService);
			thread.setIdList(idList);
			thread.setCountDownLatch(countDownLatch);
			executorService.submit(thread);
		}
		countDownLatch.await();
		executorService.shutdown();

		long endTime = System.currentTimeMillis();    //获取结束时间

		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
/*
		//所有的bean,参考：http://412887952-qq-com.iteye.com/blog/2314051
		String[] beanNames = ctx.getBeanDefinitionNames();
		//String[] beanNames = ctx.getBeanNamesForAnnotation(RestController.class);//所有添加该注解的bean
		log.info("bean总数:{}", ctx.getBeanDefinitionCount());
		int i = 0;
		for (String str : beanNames) {
			log.info("{},beanName:{}", ++i, str);
		}*/
	}
}
