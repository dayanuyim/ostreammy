package com.dayanuyim.ostreammy;

import java.io.File;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.BasicConfigurator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
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
		System.out.println("init map");
	}

	@GET
    public String show(){
        return "Thanks to use the Map. Please specify a map to display";
    }   
	
	/*
	@Bean
	@Qualifier("repo")
	public String repoPath(){
		//for testing only, should be from properties
		if(SystemUtils.IS_OS_WINDOWS)
			return "C:\\Users\\tsungtatsai.III\\web\\repo";
		else if(SystemUtils.IS_OS_LINUX)
			return "/home/tsungtatsai/web/repo";
		return "web/repo";
	}
	*/

	@Bean
	@Qualifier("repo")
	public File repoLocation(){
		if(SystemUtils.IS_OS_WINDOWS)
			return new File("C:\\Users\\tsungtatsai.III\\web\\repo");
		else if(SystemUtils.IS_OS_LINUX)
			return new File("/home/tsungtatsai/web/repo");
		return new File("web/repo");
	}
}
