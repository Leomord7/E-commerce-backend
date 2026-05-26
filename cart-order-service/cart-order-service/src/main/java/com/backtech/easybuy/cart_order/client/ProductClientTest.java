package com.backtech.easybuy.cart_order.client;

import com.backtech.easybuy.cart_order.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="product-service", url="http://localhost:8081/api")
public interface ProductClientTest {
    @GetMapping("/products/{productId}")
    ProductResponse getProductById(@PathVariable("productId") String productId);
}
