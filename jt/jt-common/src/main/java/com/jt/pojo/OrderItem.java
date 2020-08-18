package com.jt.pojo;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;
@TableName("tb_order_item")
@Data
@Accessors(chain=true)
public class OrderItem extends BasePojo{
	
	@TableId	//POJO对象中的主键只能有一个  高版本的新规定.
    private String itemId;
	
    private String orderId;

    private Integer num;

    private String title;

    private Long price;

    private Long totalFee;

    private String picPath;
}