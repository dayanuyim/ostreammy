package com.dayanuyim.ostreammy.config;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.dayanuyim.ostreammy.App;
import com.dayanuyim.ostreammy.annotation.Location;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses=com.dayanuyim.ostreammy.App.class)
public class SpringConfig extends WebMvcConfigurerAdapter
{
	
	//properties =============================
	@Configuration
    @Profile("default")
    @PropertySource("classpath:/app.properties")
    static class Default
    {
		static{
			System.out.println("Spring property source: default");
		}
    }

    @Configuration
    @Profile("dev")
    @PropertySource({"classpath:/app.properties", "classpath:/app-dev.properties"})
    static class Dev
    {
    	static{
    		System.out.println("Spring property source: dev");
    	}
    }

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	//static page ===================
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	//jsp page ===================
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setExposeContextBeansAsAttributes(true);
		return resolver;
	}


	// bean =========================
	@Bean
	@Qualifier("repo") @Location
	public File repoLocation(@Value("${repo.path}") String path){
		if(StringUtils.isBlank(path))
			throw new RuntimeException("No repo path specified");
		return new File(path);
	}
}
