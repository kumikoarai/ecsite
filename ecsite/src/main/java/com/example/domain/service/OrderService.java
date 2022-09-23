package com.example.domain.service;

import java.util.List;

import com.example.entity.OrderDetail;
import com.example.entity.ProductOrder;

public interface OrderService {

	/** オーダーの登録*/
	public Integer postProductOrder(ProductOrder productOrder);

	/** オーダーの詳細の登録*/
	public void postOrderDetail(OrderDetail orderDetail);

	/** オーダーの取得*/
	public List<ProductOrder> getProductOrder();

	/** オーダーの取得 (1件) オーダーID別*/
	public ProductOrder getProductOrderOneByOrderId(Integer orderId);

	/** オーダーの詳細の取得*/
	public List<OrderDetail> getOrderDetail();

	/** オーダーの詳細の取得 オーダーID別*/
	public List<OrderDetail> getOrderDetailByOrderId(Integer orderId);

	/** オーダーの取得 ユーザーID別*/
	public List<ProductOrder> getProductOrderByUserId(Integer userId);
}
