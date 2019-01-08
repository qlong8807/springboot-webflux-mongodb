package com.xa.jans.config;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MyHandler {

	public Mono<ServerResponse> getTime(ServerRequest request) {
		return ok().contentType(MediaType.TEXT_PLAIN)
				.body(Mono.just("now is:" + new SimpleDateFormat("hh:mm:ss").format(new Date())), String.class);
	}
	public Mono<ServerResponse> getDate(ServerRequest request) {
		return ok().contentType(MediaType.TEXT_PLAIN)
				.body(Mono.just("now is:" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())), String.class);
	}

	public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {
		//TEXT_EVENT_STREAM会持续的给客户端发送消息，HTML5新特性。如果返回json查看UserController.findAll2().
		return ok().contentType(MediaType.TEXT_EVENT_STREAM).body( // 1
				Flux.interval(Duration.ofSeconds(1)). // 2
						map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date())),
				String.class);
	}
}
