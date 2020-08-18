package com.jt.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Cart;

public interface CartMapper extends BaseMapper<Cart>{
	
	@Update("update tb_cart set num=#{num},updated =#{date} where id = #{id}")
	void updateCartNum(Long id, Integer num, Date date);

}
