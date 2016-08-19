package com.dayanuyim.ostreammy.config;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.dayanuyim.ostreammy.annotation.Location;

@Configuration
@Import({DefaultPropertiesConfig.class, DevPropertiesConfig.class}) //dev props, if any, should be after def props
@ComponentScan(basePackageClasses={com.dayanuyim.ostreammy.App.class},
				excludeFilters={ @Filter(type=FilterType.ANNOTATION, value=EnableWebMvc.class) })
public class RootConfig {

	static {
		BasicConfigurator.configure();
	}

	// bean =========================
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

	@Bean
	@Qualifier("preload") @Location
	/*
	public File preloadLocation(@Value("#{repoLocation}") File repo){
		return new File(repo, "_preload");
	}
	*/
	public File preloadLocation(@Value("${preload.path}") String path){
		if(StringUtils.isBlank(path))
			throw new RuntimeException("No repo path specified");
		return new File(path);
	}
}
