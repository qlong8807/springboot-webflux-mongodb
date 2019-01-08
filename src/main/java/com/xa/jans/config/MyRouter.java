package com.xa.jans.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MyRouter {

	@Autowired
	private MyHandler myHandler;

	@Bean
	public RouterFunction<ServerResponse> myRouter1() {
		return RouterFunctions.route(RequestPredicates.GET("/time"), myHandler::getTime)
				.andRoute(RequestPredicates.GET("/date"), myHandler::getDate)
				.andRoute(RequestPredicates.GET("/times"), myHandler::sendTimePerSec);
	}
}
