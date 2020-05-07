package com.yc.springcloud.hystrix.service;

import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

/**
 * 新建一个名为HelloServiceObserveCommand的类，来继承Hystrix给我们提供的HystrixObservableCommand类，
 * 同样HelloServiceObserveCommand类也不是交由Spring管理的，需要我们通过Controller层注入RestTemplate，放在构造方法来注入
 * 源辰信息
 * @author navy
 * @2019年8月9日
 */
public class HelloServiceObserveCommand extends HystrixObservableCommand<String>{
	private RestTemplate restTemplate;

	public HelloServiceObserveCommand(String commandGroupKey, RestTemplate restTemplate) {
		super(HystrixCommandGroupKey.Factory.asKey(commandGroupKey));
		this.restTemplate = restTemplate;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Observable<String> construct() {
		//观察者订阅网络请求事件
		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				try {
					if (!subscriber.isUnsubscribed()){
						System.out.println("方法执行....");
						String result = restTemplate.getForEntity("http://hello-service/hello", String.class).getBody();
						
						//这个方法监听方法，是传递结果的，请求多次的结果通过它返回去汇总起来。
						subscriber.onNext(result);
						String result1 = restTemplate.getForEntity("http://hello-service/hello", String.class).getBody();
						
						//这个方法是监听方法，传递结果的
						subscriber.onNext(result1);
						subscriber.onCompleted();
					}
				} catch (Exception e) {
					subscriber.onError(e);
				}
			}
		});
	}
	
	//服务降级Fallback
	@SuppressWarnings("deprecation")
	@Override
	protected Observable<String> resumeWithFallback() {
		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				try {
					if (!subscriber.isUnsubscribed()) {
						subscriber.onNext("error");
						subscriber.onCompleted();
					}
				} catch (Exception e) {
					subscriber.onError(e);
				}
			}
		});
	}
}