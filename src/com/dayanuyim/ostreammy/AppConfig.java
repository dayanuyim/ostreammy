package com.dayanuyim.ostreammy;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;

import org.apache.log4j.BasicConfigurator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ApplicationPath("/")
public class AppConfig extends ResourceConfig
{
	static {
		BasicConfigurator.configure();
	}

	public AppConfig(){
		packages("com.dayanuyim.ostreammy");
		register(JspMvcFeature.class);
		register(MultiPartFeature.class);
		//register(LoggingFilter.class);
		System.out.println("init map");
	}

	@GET
    public String show(){
        return "Thanks to use the Map. Please specify a map to display";
    }   
}
