package com.dayanuyim.ostreammy;

import java.io.File;

import javax.annotation.Resource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.dayanuyim.ostreammy.annotation.Location;

@Configuration
@PropertySource("classpath:/app.properties")
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
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}


	@Bean
	@Qualifier("repo") @Location
	public File repoLocation(@Value("${repo.path}") String path){
		if(StringUtils.isBlank(path))
			throw new RuntimeException("No repo path specified");
		return new File(path);
	}
}
