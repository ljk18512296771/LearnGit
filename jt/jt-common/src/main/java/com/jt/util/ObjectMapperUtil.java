package com.jt.util;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	
	//json与对象的转化   优化异常处理
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//1.将对象转化为JSON
	public static String toJSON(Object target) {
		
		if(target == null) {
			throw new NullPointerException("taget数据为null");
		}
		
		try {
			return MAPPER.writeValueAsString(target);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e); //如果转化过程中有问题则直接抛出异常
		}
		
	}


	//2. 将json串转化为对象   用户传递什么样的类型,就返回什么样的对象!!!
	// <T>  定义了一个泛型对象  代表任意类型
	public static <T> T toObject(String json,Class<T> targetClass) {
		
		if(StringUtils.isEmpty(json) || targetClass == null) {
			throw new NullPointerException("参数不能为null");
		}
		
		try {
			//class类型---通过反射机制---实例化对象-----setKey为属性赋值.
			return MAPPER.readValue(json, targetClass);
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
