package com.dayanuyim.map;

import javax.ws.rs.ApplicationPath;

import org.apache.log4j.BasicConfigurator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ApplicationPath("/map")
public class AppConfig extends ResourceConfig
{
	public AppConfig(){
		packages("com.dayanuyim.map");
		register(JspMvcFeature.class);
		//register(MultiPartFeature.class);
		//register(LoggingFilter.class);
		BasicConfigurator.configure();
		System.out.println("init map");
	}
}
