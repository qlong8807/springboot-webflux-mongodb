package com.xa.jans;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.xa.jans.entity.User;

import reactor.core.publisher.Mono;

public class ReactiveClientTest {

	@Test
	public void test() {
		//subscribe方法支持多个参数，正常数据、错误数据、完成信号。
		Mono.just(new Exception("a error")).subscribe(System.out::println, System.err::println,
				() -> System.out.println("complate"));
	}

	/**
	 * 测试返回Mono
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test1() throws InterruptedException {
		WebClient client = WebClient.create("http://localhost:8080");
		Mono<String> resp = client.get().uri("/hello").retrieve()// 异步地获取response信息
				.bodyToMono(String.class);// 将response body解析为字符串
		resp.subscribe(System.out::println);
		TimeUnit.SECONDS.sleep(1);
		// 由于是异步的，我们将测试线程sleep 1秒确保拿到response，也可以用CountDownLatch。
	}

	/**
	 * Flux
	 */
	@Test
	public void test2() {
		WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();
		client.get().uri("/user/findALl2").accept(MediaType.APPLICATION_STREAM_JSON)// 配置请求Header：Content-Type:application/stream+json
				.exchange()// 获取response信息，返回值为ClientResponse，retrive()可以看做是exchange()方法的“快捷版”
				.flatMapMany(response -> response.bodyToFlux(User.class))// 使用flatMap来将ClientResponse映射为Flux
				.doOnNext(System.out::println)// 只读地peek每个元素，然后打印出来，它并不是subscribe，所以不会触发流
				.blockLast();// 上个例子中sleep的方式有点low，blockLast方法，顾名思义，在收到最后一个元素前会阻塞，响应式业务场景中慎用
	}

	@Test
	public void webClientTest3() throws InterruptedException {
		WebClient webClient = WebClient.create("http://localhost:8080");
		webClient.get().uri("/times").accept(MediaType.TEXT_EVENT_STREAM) // 1
				.retrieve().bodyToFlux(String.class).log() // 这次用log()代替doOnNext(System.out::println)来查看每个元素
				.take(10) // 由于/times是一个无限流，这里取前10个，会导致流被取消
				.blockLast();
	}
}
