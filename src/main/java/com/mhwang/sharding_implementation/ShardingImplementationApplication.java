package com.mhwang.sharding_implementation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ShardingImplementationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShardingImplementationApplication.class, args);
	}

}
