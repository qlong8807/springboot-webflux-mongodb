package com.xa.jans.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import com.xa.jans.entity.MyEvent;

import reactor.core.publisher.Flux;

@Repository
public interface MyEventRepository extends ReactiveMongoRepository<MyEvent, Long>{
	
	//@Tailable注解的作用类似于linux的tail命令，被注解的方法将发送无限流，需要注解在返回值为Flux这样的多个元素的Publisher的方法上
	@Tailable
	public Flux<MyEvent> findBy();

}
