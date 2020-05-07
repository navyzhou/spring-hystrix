package com.yc.springcloud.hystrix.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.yc.springcloud.hystrix.service.HelloServiceCommand;

/**
 * 顾客、消费控制类
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
		HelloServiceCommand command = new HelloServiceCommand("hi",restTemplate);
        String result = command.execute(); // 方法来手工执行的
        return result;
	}
}