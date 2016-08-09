package com.dayanuyim.ostreammy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//@Profile("default")
@PropertySource("classpath:/app.properties")
public class DefaultPropertiesConfig {
	static{
		System.out.println("Spring property source: default");
	}
}
