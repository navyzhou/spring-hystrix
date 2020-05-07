package com.yc.springcloud.eureka.controller;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicContorller {
	@Value("${server.port}") // 引用注入配置文件中的端口号
	private String port;
	
	@RequestMapping("/")
	public String home() {
		return "hello from port " + port;
	}
	
	@RequestMapping("/info")
	public void intfo(HttpServletResponse response) {
		try {
			response.sendRedirect("/index.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/hello")
	public String hello() {
		return "hello client : " + port;
	}
	
	@RequestMapping("/rd")
	public int rd() {
		Random rd = new Random();
		return rd.nextInt(100);
	}
}
