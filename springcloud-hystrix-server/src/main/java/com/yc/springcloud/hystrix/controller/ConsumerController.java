package com.yc.springcloud.hystrix.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yc.springcloud.hystrix.web.IHelloClient;

/**
 * 顾客、消费控制类
 * 源辰信息
 * @author navy
 * @2019年8月9日
 */
@RestController
public class ConsumerController {
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IHelloClient helloClient;


	@RequestMapping("/hi")
	// 请求熔断注解，当服务出现问题时候会执行fallbackMethod属性的名为helloFallBack的方法
	@HystrixCommand(fallbackMethod = "helloFallBack")
	public String helloService() throws ExecutionException, InterruptedException {
		return restTemplate.getForEntity("http://client-service/hello",String.class).getBody();
	}
	
	/**
	 * 服务熔断后调用的方法
	 * @return
	 */
	public String helloFallBack(){
		return "cloud-client服务器熔断了...";
	}
	
	@GetMapping("/hello")
	@HystrixCommand(fallbackMethod = "helloFallBack")
	public String hello(){
		return helloClient.hello();
	}
}