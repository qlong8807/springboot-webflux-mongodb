package com.xa.jans.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xa.jans.entity.User;
import com.xa.jans.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author zyl
 * @date 2019年1月7日
 * @desc 原来返回User的话，那现在就返回Mono<User>；原来返回List<User>的话，那现在就返回Flux<User>。
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("")
	public Flux<User> findAll(){
		return userService.findAll();
	}
	@GetMapping("findAll1")
	public Flux<User> findAll1(){
		return userService.findAll().delayElements(Duration.ofSeconds(1));
		//每个元素都延迟，可以看到也不是响应式的。
	}
	//由于这里返回的是JSON，因此不能使用TEXT_EVENT_STREAM，而是使用APPLICATION_STREAM_JSON，即application/stream+json格式。
	@GetMapping(value="findAll2",produces=MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<User> findAll2(){
		return userService.findAll().log().delayElements(Duration.ofSeconds(1));
	}
	@GetMapping("/{name}")
	public Mono<User> findByName(@PathVariable String name){
		return userService.findByName(name);
	}
	@DeleteMapping("/{name}")
	public Mono<Long> deleteByName(@PathVariable String name){
		return userService.deleteByName(name);
	}
	@PostMapping("")
	public Mono<User> save(User user){
		return userService.save(user);
	}
}
