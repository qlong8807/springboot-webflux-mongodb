package com.xa.jans.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;

import com.xa.jans.entity.MyEvent;

@Configuration
public class MyEventStreamHandler {

	
	/**
	 * MongoOperations提供对MongoDB的操作方法，由Spring注入的mongo实例已经配置好，直接使用即可；
		CommandLineRunner也是一个函数式接口，其实例可以用lambda表达；
		如果有，先删除collection，生产环境慎用这种操作；
		创建一个记录个数为10的capped的collection，容量满了之后，新增的记录会覆盖最旧的。
	 * @param mongo
	 * @return 
	 */
	@Bean
	public CommandLineRunner initConfig(MongoOperations mongo) {
		return (String... args)->{
			mongo.dropCollection(MyEvent.class);
			mongo.createCollection(MyEvent.class,CollectionOptions.empty().maxDocuments(100).size(100000).capped());
		};
	}
}
