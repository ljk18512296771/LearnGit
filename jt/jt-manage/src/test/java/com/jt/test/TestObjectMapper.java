package com.jt.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

public class TestObjectMapper {
	
	//改对象就是工具api  有且只有一份即可.并且不允许别人修改.
	private static final ObjectMapper MAPPER = new ObjectMapper();
	/**
	 * 目的: 实现对象与json串之间的转化
	 * 步骤1:  将对象转化为json
	 * 步骤2:  将json转化为对象 
	 * 利用ObjectMapper 工具API实现
	 * @throws JsonProcessingException 
	 */
	@Test
	public void test01() throws JsonProcessingException {
		//1.定义对象
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(101L).setItemDesc("json转化测试")
		.setCreated(new Date()).setUpdated(itemDesc.getCreated());
		
		//1.将对象转化为JSON   调用的是对象的get方法
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//2.将json转化为对象   传递需要转化之后的class类型   调用是对象的set方法
		ItemDesc itemDesc2 = MAPPER.readValue(json, ItemDesc.class);
		System.out.println(itemDesc2.getItemDesc());
		
	}
	
	public List<ItemDesc> initList() {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(101L).setItemDesc("json转化测试")
		.setCreated(new Date()).setUpdated(itemDesc.getCreated());
		
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(101L).setItemDesc("json转化测试")
		.setCreated(new Date()).setUpdated(itemDesc2.getCreated());
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		list.add(itemDesc);
		list.add(itemDesc2);
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testList2JSON() throws JsonProcessingException {
		List<ItemDesc> list = initList();
		String json = MAPPER.writeValueAsString(list);
		System.out.println(json);
		List<ItemDesc>  list2 = MAPPER.readValue(json, list.getClass());
		System.out.println(list2);
	}
	
	
	
	
	
}
