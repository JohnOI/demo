package com.example.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication /* extends RouteBuilder */ {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

/*
	@Override
	public void configure() throws Exception {
		from("file:/Users/johni/Documents/cameltest/in/")
				.to("file:/Users/johni/Documents/cameltest/out/");

	}
 */

}
