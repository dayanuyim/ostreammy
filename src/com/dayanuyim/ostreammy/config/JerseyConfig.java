package com.dayanuyim.ostreammy.config;

import javax.ws.rs.ApplicationPath;

import org.apache.log4j.BasicConfigurator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

@ApplicationPath(JerseyConfig.URI_PATH)
public class JerseyConfig extends ResourceConfig
{
	public static final String URI_PATH = "/ostreammy";

	static {
		BasicConfigurator.configure();
	}

	public JerseyConfig(){
		packages("com.dayanuyim.ostreammy");
		register(JspMvcFeature.class);
		register(MultiPartFeature.class);
		System.out.println("init map");
	}
}
