package com.jt.mapper;


import java.util.Date;

import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Order;

public interface OrderMapper extends BaseMapper<Order>{
	@Update("update tb_order set status=6,updated=now() "
			+ "where status = 1 and created < #{timeOut}")
	void updateStatus(Date timeOut);
	
}