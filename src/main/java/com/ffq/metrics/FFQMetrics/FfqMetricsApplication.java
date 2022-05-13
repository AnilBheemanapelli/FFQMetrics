package com.ffq.metrics.FFQMetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FfqMetricsApplication extends SpringBootServletInitializer{

	 @Override
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	      return application.sources(FfqMetricsApplication.class);
	   }
	public static void main(String[] args) {
		SpringApplication.run(FfqMetricsApplication.class, args);
	}

}
