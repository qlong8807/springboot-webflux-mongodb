package com.xa.jans.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xa.jans.entity.MyEvent;
import com.xa.jans.repository.MyEventRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author zyl
 * @date 2019年1月8日
 * @desc 启动后会清空MongoDB中的events数据
 * 1.运行一次com.xa.jans.MyEventControllerTest.test()方法插入几条数据；
 * 2.命令行执行curl http://localhost:8080/events可以看到刚插入的数据，并且流没有断开；
 * 3.再次执行com.xa.jans.MyEventControllerTest.test()方法插入几条数据；
 * 4.在命令行中就能看到数据一条条的出现了。
 */
@RestController
@RequestMapping(value="events")
public class MyEventController {

	@Autowired
	private MyEventRepository myEventRepository;
	
	@GetMapping(path="",produces=MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<MyEvent> get(){
//		return myEventRepository.findAll();
		//findALl不能实现无限流发送，所以需要自定义方法
		return myEventRepository.findBy();
		//@Tailable仅支持有大小限制的（“capped”）collection，而自动创建的collection是不限制大小的，
		//因此我们需要先手动创建。Spring Boot提供的CommandLineRunner可以帮助我们实现这一点。
	}
	
	/**
	 * 
	 * @param events
	 * @return 用一个Mono<Void>作为方法返回值，表示如果传输完的话只给一个“完成信号”就OK了
	 * 
    指定传入的数据是application/stream+json，与getEvents方法的区别在于这个方法是consume这个数据流；
    insert返回的是保存成功的记录的Flux，但我们不需要，使用then方法表示“忽略数据元素，只返回一个完成信号”。
	 */
	@PostMapping(value="",consumes=MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<Void> save(@RequestBody Flux<MyEvent> events){
//		System.err.println(events.blockFirst());
		Mono<Void> mono = myEventRepository.insert(events).then();
		return mono;
	}
}
