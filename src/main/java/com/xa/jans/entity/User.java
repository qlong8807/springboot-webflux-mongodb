package com.xa.jans.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data// 生成无参构造方法/getter/setter/hashCode/equals/toString
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {
	@Id
	private String id;
	@Indexed(unique=true)
	private String name;
	private int age;
	private String email;
}
