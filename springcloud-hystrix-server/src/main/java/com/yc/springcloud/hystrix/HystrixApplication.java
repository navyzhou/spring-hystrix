package com.yc.springcloud.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;

/**
 * Spring Cloud Ribbon是一个基于HTTP和TCP的客户端负载均衡工具，它基于Netflix Ribbon实现。通过Spring Cloud的封装，
 * 可以让我们轻松地将面向服务的REST模版请求自动转换成客户端负载均衡的服务调用。Spring Cloud Ribbon虽然只是一个工具类框架，
 * 它不像服务注册中心、配置中心、API网关那样需要独立部署，但是它几乎存在于每一个Spring Cloud构建的微服务和基础设施中。
 * 因为微服务间的调用，API网关的请求转发等内容，实际上都是通过Ribbon来实现的，包括后续我们将要介绍的Feign，它也是基于Ribbon实现的工具。
 * 所以，对Spring Cloud Ribbon的理解和使用，对于我们使用Spring Cloud来构建微服务非常重要。
 * 源辰信息
 * @author navy
 * @2019年8月9日
 */
@SpringBootApplication
@EnableDiscoveryClient   // discovery 发现: 发觉
//允许断路器
@EnableCircuitBreaker  // 负责开启 Netflix Hystrix 熔断机制
@EnableFeignClients
@EnableHystrixDashboard //添加这个仪表盘注解
public class HystrixApplication {
	public static void main(String[] args) {
		SpringApplication.run(HystrixApplication.class, args);
	}

	/**
	 * IRule是根据特定算法从服务列表中选取一个要访问的服务。SpringCloud自带7中算法
	 * 1.RoundRobinRule--轮询
	 * 2.RandomRule--随机
	 * 3.AvailabilityFilteringRule --会先过滤掉由于多次访问故障处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务，然后对于剩余的服务列表按照轮询的策略进行访问
	 * 4.WeightedResponseTimeRul--根据平均响应时间计算所有服务的权重，响应时间越快服务权重越大被选中的概率越大。刚启动时如果同统计信息不足，则使用轮询的策略，等统计信息足够会切换到自身规则
	 * 5.RetryRule-- 先按照轮询的策略获取服务，如果获取服务失败则在指定的时间内会进行重试，获取可用的服务
	 * 6.BestAvailableRule --会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量小的服务
	 * 7.ZoneAvoidanceRule -- 默认规则，复合判断Server所在区域的性能和Server的可用行选择服务器。
	 * @return
	 */
	@Bean
	public IRule ribbonRule(){
		return new RoundRobinRule();
	}

	/**
	 * Spring Cloud的commons模块提供了一个@LoadBalanced注解，方便我们对RestTemplate添加一个LoadBalancerClient，
	 * 以实现客户端负载均衡。通过源码可以发现这是一个标记注解
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	// 定义仪Hystrix表盘
	@Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<>(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }

}
