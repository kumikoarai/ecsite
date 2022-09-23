package com.example.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.service.OrderService;
import com.example.entity.OrderDetail;
import com.example.entity.ProductOrder;
import com.example.repository.OrderDetailRepository;
import com.example.repository.ProductOrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository productOrderRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;



	/** オーダーの登録*/
	@Transactional
	@Override
	public Integer postProductOrder(ProductOrder productOrder) {
		ProductOrder po = productOrderRepository.saveAndFlush(productOrder);
		return po.getOrderId();
	}



	/** オーダーの詳細の登録*/
	@Transactional
	@Override
	public void postOrderDetail(OrderDetail orderDetail) {
		orderDetailRepository.save(orderDetail);
	}



	/** オーダーの取得*/
	@Override
	public List<ProductOrder> getProductOrder() {
		List<ProductOrder> productOrderList = productOrderRepository.findAllByOrderByOrderIdDesc();
		return productOrderList;
	}



	/** オーダーの詳細の取得*/
	@Override
	public List<OrderDetail> getOrderDetail() {
		List<OrderDetail> OrderDetailList = orderDetailRepository.findAll();
		return OrderDetailList;
	}



	/** オーダーの詳細の取得 オーダーID別*/
	@Override
	public List<OrderDetail> getOrderDetailByOrderId(Integer orderId) {
		List<OrderDetail> OrderDetailList = orderDetailRepository.findByOrderId(orderId);
		return OrderDetailList;
	}



	/** オーダーの取得 (1件) オーダーID別*/
	@Override
	public ProductOrder getProductOrderOneByOrderId(Integer orderId) {
		Optional<ProductOrder> option = productOrderRepository.findById(orderId);
		ProductOrder productOrder = option.orElse(null);
		return productOrder;
	}



	/** オーダーの取得 ユーザーID別*/
	@Override
	public List<ProductOrder> getProductOrderByUserId(Integer userId) {
		List<ProductOrder> productOrderList = productOrderRepository.findByUserId(userId);

		return productOrderList;
	}

}
