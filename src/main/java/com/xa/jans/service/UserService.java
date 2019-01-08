package com.xa.jans.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xa.jans.entity.User;
import com.xa.jans.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * 保存或更新。 如果传入的user没有id属性，由于username是unique的，在重复的情况下有可能报错，
	 * 这时找到以保存的user记录用传入的user更新它。
	 */
	public Mono<User> save(User user) {
		return userRepository.save(user)
				.onErrorResume(e -> userRepository.findUserByName(user.getName()).flatMap(originUser -> {
					user.setId(originUser.getId());
					return userRepository.save(user);
				}));
	}
	public Flux<User> findAll(){
		return userRepository.findAll();
	}
	public Mono<User> findByName(String name){
		return userRepository.findUserByName(name);
	}
	public Mono<Long> deleteByName(String name){
		return userRepository.deleteUserByName(name);
	}
}
