package com.wind;

import com.wind.common.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
@MapperScan(basePackages = "com.wind.mybatis.mapper")
public class SeedApplication {
	public static void main(String[] args) {
		// SpringApplication.run(SeedApplication.class, args);
		ApplicationContext app = SpringApplication.run(SeedApplication.class, args);
		SpringUtil.setApplicationContext(app);
	}
}
