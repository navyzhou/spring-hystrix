package com.yc.springcloud.hystrix.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.yc.springcloud.hystrix.service.HelloServiceObserveCommand;

import rx.Observable;
import rx.Observer;

/**
 * 前面的例子有异步和同步这两种方式，这里HystrixObservableCommand也有两个中执行方式，分别是，冷执行，和热执行
 * 刚刚HystrixObservableCommand中的command.observe()热执行方式。
 * 什么是热执行方式呢？
 * 	所谓的热执行就是不管你事件有没有注册完(onCompleted()，onError，onNext这三个事件注册)，
 * 就去执行我的业务方法即(HystrixObservableCommand实现类中的construct()方法).
 * 我们可以在上面的代码中sleep（10000）一下清楚看出热执行.
 * 运行结果可以看到，是先执行了业务方法，在卡顿了10秒后才时间监听方法才执行
 * 
 * 什么是冷执行呢？
 * 所谓的冷执行就是，先进行事件监听方法注册完成后，才执行业务方法
 * 	接下来我们把Controller中的Observable<String> observable = command.observe();
 * 改成冷执行Observable<String> observable =command.toObservable();
 * 运行结果如下：先卡顿了10S后，才出现结果
 * 源辰信息
 * @author navy
 * @2019年8月9日
 */
@RestController
public class ConsumerController {
	@Autowired
	private  RestTemplate restTemplate;

	@RequestMapping("/hi")
	public String helloConsumer() throws ExecutionException, InterruptedException {
		List<String> list = new ArrayList<>();
		HelloServiceObserveCommand command = new HelloServiceObserveCommand("hello",restTemplate);
		//热执行
		Observable<String> observable = command.observe();
		//冷执行
		//	Observable<String> observable =command.toObservable();
		Thread.sleep(10000);
		//订阅
		observable.subscribe(new Observer<String>() {
			//请求完成的方法
			@Override
			public void onCompleted() {
				System.out.println("会聚完了所有查询请求");
			}

			@Override
			public void onError(Throwable throwable) {
				throwable.printStackTrace();
			}
			//订阅调用事件，结果会聚的地方，用集合去装返回的结果会聚起来。
			@Override
			public void onNext(String s) {
				System.out.println("结果来了.....");
				list.add(s);
			}
		});

		return list.toString();
	}
}