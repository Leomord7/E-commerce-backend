package com.backtech.easybuy.cart_order.service;

import java.util.List;
import java.util.UUID;

import com.backtech.easybuy.cart_order.dto.CheckoutRequest;
import com.backtech.easybuy.cart_order.dto.OrderCreateRequest;
import com.backtech.easybuy.cart_order.dto.OrderResponse;
import com.backtech.easybuy.cart_order.dto.ProductResponse;

public interface OrderService {


	ProductResponse createOrder(OrderCreateRequest orderCreateRequest) ;

	OrderResponse checkout(String userId, CheckoutRequest request);

	OrderResponse getOrderById(Long orderId);

	OrderResponse getOrderByNumber(String orderNumber);

	List<OrderResponse> getOrdersByUserId(String userId);

	OrderResponse cancelOrder(Long orderId);

	void releaseReservedStock(UUID productId, Integer quantity);
}
