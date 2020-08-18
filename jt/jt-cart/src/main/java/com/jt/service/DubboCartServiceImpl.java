package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubboCartServiceImpl implements DubboCartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}

	//update tb_cart set num = #{num},updated = #{updated} where user_id=#{userId} and 
	//item_id =#{itemId}
	@Override
	@Transactional
	public void updateCartNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum())
				.setUpdated(new Date());  //更新时间
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("item_id", cart.getItemId())
					 .eq("user_id", cart.getUserId());
		//根据对象中不为null的属性 设定set条件
		cartMapper.update(cartTemp, updateWrapper);
	}

	//1.第一次架构 入库
	//2.第N次架构 更新数据库
	@Override
	@Transactional
	public void saveCart(Cart cart) {
		
		//1.先查询数据库中是否有该记录  itemId和userId
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", cart.getUserId());
		queryWrapper.eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		//cartDB几乎所有的数据都不为null 将来可能被当做条件使用.
		if(cartDB == null) {
			//说明第一次加购
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//只更新商品数量
			int num = cart.getNum() + cartDB.getNum();
			/**Cart cartTemp = new Cart();
			cartTemp.setId(cartDB.getId())
					.setNum(num)
					.setUpdated(new Date());
			cartMapper.updateById(cartTemp); //根据主键更新数据库.
			**/
			//自己手动操作Sql
			cartMapper.updateCartNum(cartDB.getId(),num,new Date());
		}
		
	}

	@Override
	public void deleteCart(Cart cart) {
		
		//让对象中不为null的属性充当where条件
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
		cartMapper.delete(queryWrapper);
	}
	
	
	
	
	
	
	
}
