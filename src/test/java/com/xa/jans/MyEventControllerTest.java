package com.xa.jans;

import java.time.Duration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.xa.jans.entity.MyEvent;

import reactor.core.publisher.Flux;

public class MyEventControllerTest {

	@Test
	public void test() {
		Flux<MyEvent> eventFlux = Flux.interval(Duration.ofSeconds(1))
				.map(l -> new MyEvent(System.currentTimeMillis(), "message-" + l)).take(5); //声明速度为每秒一个MyEvent元素的数据流，不加take的话表示无限个元素的数据流；
		WebClient webClient = WebClient.create("http://localhost:8080");
		webClient.post().uri("/events").contentType(MediaType.APPLICATION_STREAM_JSON) // 2
				.body(eventFlux, MyEvent.class) //body方法设置请求体的数据
				.retrieve().bodyToMono(Void.class).block();
	}

}
