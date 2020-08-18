package com.jt.aop;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

//标识改类是全局异常处理机制的配置类
@RestControllerAdvice //advice通知   返回的数据都是json串
@Slf4j	//添加日志
public class SystemExceptionAOP {
	
	/*
	 * 添加通用异常返回的方法.
	 * 底层原理:AOP的异常通知.
	 * 
	 * 常规手段:  SysResult.fail();
	 * 跨域访问:  JSONP   必须满足JSONP跨域访问要求  callback(SysResult.fail())
	 * 问题: 如何断定用户使用的是跨域方式还是普通业务调用??? 
	 * 分析: jsonp请求   get请求的方式携带?callback   
	 * 判断依据:  用户参数是否携带callback  特定参数,一般条件下不会使用
	 * */
	@ExceptionHandler({RuntimeException.class}) //拦截运行时异常
	public Object systemResultException(HttpServletRequest request,Exception exception) {
		
		String callback = request.getParameter("callback");
		if(StringUtils.isEmpty(callback)) {	//不是跨域访问
			log.error("{~~~~~~"+exception.getMessage()+"}", exception); //输出日志
			return SysResult.fail();	 //返回统一的失败数据
		}
		
		//说明:有可能跨域  jsonp只能提交GET请求
		String method = request.getMethod();
		if(!method.equalsIgnoreCase("GET")) {
			//如果不是get请求,不是jsonp请求方式
			log.error("{~~~~~~"+exception.getMessage()+"}", exception); //输出日志
			return SysResult.fail();	 //返回统一的失败数据
		}
		
		//3.如果程序执行到这里,标识进行了JSONP的请求. 按照JSONP的方式返回数据
		log.error("{~~~~~~"+exception.getMessage()+"}", exception); //输出日志
		return new JSONPObject(callback, SysResult.fail());
		
	}
	
	

}
