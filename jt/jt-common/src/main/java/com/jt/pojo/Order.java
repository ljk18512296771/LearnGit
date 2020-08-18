package com.jt.pojo;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_order")
@Data
@Accessors(chain=true)
public class Order extends BasePojo{
	@TableField(exist=false)	//入库操作忽略该字段
	private OrderShipping orderShipping;
								//封装订单商品信息  一对多
	@TableField(exist=false)	//入库操作忽略该字段
	private List<OrderItem> orderItems;  //orderItems[0].xxxx  orderItems[0].yyyy orderItems[0].zzzz
									     //orderItems[1].xxxx  orderItems[1].yyyy orderItems[1].zzzz
	
	@TableId		//标识主键信息   没有设定自增
    private String orderId;   //Long 设定自增 给数据带来压力  String 订单号：登录用户id+当前时间戳',
    private String payment;
    private Integer paymentType;
    private String postFee;
    private Integer status;   //'状态：1、未付款2、已付款3、未发货4、已发货5、交易成功6、交易关闭',
    private Date paymentTime;
    private Date consignTime;
    private Date endTime;
    private Date closeTime;
    private String shippingName;
    private String shippingCode;
    private Long userId;
    private String buyerMessage;
    private String buyerNick;
    private Integer buyerRate;

}