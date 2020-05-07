package com.yc.eureka.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	/*
	 * 高版本的spring cloud会默认开始csrf功能
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/* http.csrf().disable()// 关闭csrf保护功能（跨域访问）
			.antMatchers("/eureka/**").permitAll();//访问eureka下无需登录认证权限
		*/
		 http.csrf().ignoringAntMatchers("/eureka/**");
	}
}
