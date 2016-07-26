package com.dayanuyim.ostreammy.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MapDescriptor {
	public MapDescriptor(){
		System.out.println("init map descriptor");
	}

	/*
	@Bean
	public static MapDescriptor descriptor(){
		return new MapDescriptor();
	}
	*/
}
