package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)	//标识注解 对谁生效
@Retention(RetentionPolicy.RUNTIME) //注解使用的有效期
public @interface CacheFind {
	
	public String key();			  //标识存入redis的key的前缀
	public int seconds() default 0;  //标识保存的时间 单位是秒
	
}
