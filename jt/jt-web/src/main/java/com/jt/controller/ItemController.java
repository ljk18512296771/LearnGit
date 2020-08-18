package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;

@Controller
@RequestMapping("/items")
public class ItemController {
	
	@Reference(check = false) //启动时暂时不校验提供者
	private DubboItemService itemService;
	
	/**
	 * http://www.jt.com/items/562379.html  跳转到商品页面
	 * ${item.title }  商品信息
	 * ${itemDesc.itemDesc } 商品详情信息
	 * 
	 * 业务说明: 根据商品id查询执行的商品/商品详情信息,之后在页面中展现.
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/{itemId}")
	public String findItemById(@PathVariable Long itemId,Model model) {
		
		Item item = itemService.findItemById(itemId);
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";  //item.jsp
	}
}







