package com.xa.jans;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.xa.jans.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class ReactiveClientTest {

	@Test
	public void test() {
		//subscribe方法支持多个参数，正常数据、错误数据、完成信号。
		Mono.just(new Exception("a error")).subscribe(System.out::println, System.err::println,
				() -> System.out.println("complate"));

		System.out.println("-------------");
		Mono.error(new Exception("a error")).subscribe(System.out::println, System.err::println,
				() -> System.out.println("complate"));
	}
	private Flux<String> getZipDescFlux(){
		String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2";
		return Flux.fromArray(desc.split("\\s+"));//将英文用空格拆分为字符串流
	}
	/**
	 * zip - 一对一合并
	 * 它对两个Flux/Mono流每次各取一个元素，合并为一个二元组（Tuple2）
	 * Flux的zip方法接受Flux或Mono为参数，Mono的zip方法只能接受Mono类型的参数。
	 * 
	 * 我们希望将一句话拆分为一个一个的单词并以每200ms一个的速度发出，除了flatMap和delayElements的组合外，还可以如下操作
	 * @throws InterruptedException 
	 */
	@Test
	public void testZipOperators() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);//不使用它的话，测试方法所在的线程会直接返回而不会等待数据流发出完毕
		Flux.zip(getZipDescFlux(), 
				Flux.interval(Duration.ofMillis(200)))//使用Flux.interval声明一个每200ms发出一个元素的long数据流；因为zip操作是一对一的，故而将其与字符串流zip之后，字符串流也将具有同样的速度；
		.subscribe(t -> System.out.println(t.getT1()),null,countDownLatch::countDown);//zip之后的流中元素类型为Tuple2，使用getT1方法拿到字符串流的元素；定义完成信号的处理为countDown
		countDownLatch.await(10, TimeUnit.SECONDS);
	}
	/**
	 * 同上
	 * @throws InterruptedException 
	 */
	@Test
	public void testZipOperators2() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);//不使用它的话，测试方法所在的线程会直接返回而不会等待数据流发出完毕
		Flux<Tuple2<String, Long>> zipWith = getZipDescFlux().zipWith(Flux.interval(Duration.ofMillis(200)));
		zipWith.subscribe(a -> System.out.println(a),null,countDownLatch::countDown);
		countDownLatch.await(10, TimeUnit.SECONDS);
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
