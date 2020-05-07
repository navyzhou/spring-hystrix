package com.yc.springcloud.hystrix.service;

import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 因此这里进入注解@HystrixCommand(fallbackMethod = "helloFallBack")的背后原理来实现熔断和服务降级。
 * 用我们自己手写的代码去实现熔断和服务降级。那么Hystrix给我们留下了什么样的接口呢？可以让我们自己手动更灵活的去实现熔断和服务降级。
 * Hystrix给我们提供了HystrixCommand类，让我们去继承它，去实现灵活的熔断和服务降级。
 * 
 * 问题又来了，我们知道我们继承HystrixCommand类的HelloServiceCommand 是没有交由Spring进行管理的，
 * 那么也就没法进行RestTemplate注入了。
 * 那么我们怎么做的呢？这时候读者要转过弯来了，我们为什么不通过Controller先注入，然后调用Service层的时候，
 * 通过MyHelloSerivceCommand的构造方法注入呢？因此问题就迎刃而解了。
 * 源辰信息
 * @author navy
 * @2019年8月9日
 */
public class HelloServiceCommand extends HystrixCommand<String> {
	private RestTemplate restTemplate;

	protected HelloServiceCommand(HystrixCommandGroupKey group) {
		super(group);
	}

	public HelloServiceCommand(String commandGroupKey,RestTemplate restTemplate) {
		super(HystrixCommandGroupKey.Factory.asKey(commandGroupKey));
		this.restTemplate = restTemplate;
	}

	//服务调用
	@Override
	protected String run() throws Exception {
		System.out.println(Thread.currentThread().getName());
		return restTemplate.getForEntity("http://client-service/hello",String.class).getBody();
	}

	//服务降级时所调用的Fallback()
	@Override
	protected String getFallback() {
		return "error";
	}
}