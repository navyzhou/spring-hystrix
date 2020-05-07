package com.yc.springcloud.hystrix.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="client-service")
public interface IHelloClient {
	@GetMapping("/hello")
	public String hello();
}
