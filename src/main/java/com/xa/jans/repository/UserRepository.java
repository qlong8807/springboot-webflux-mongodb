package com.xa.jans.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.xa.jans.entity.User;

import reactor.core.publisher.Mono;

/**
 * @author zyl
 * @date 2019年1月7日
 * @desc 与非响应式Spring Data的CrudReposity对应的，
 * 响应式的Spring Data也提供了相应的Repository库：ReactiveCrudReposity，
 * 当然，我们也可以使用它的子接口ReactiveMongoRepository。
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String>{
	
	Mono<User> findUserByName(String name);
	
	Mono<Long> deleteUserByName(String name);

}
