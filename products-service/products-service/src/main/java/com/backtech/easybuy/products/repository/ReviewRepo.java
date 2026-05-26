package com.backtech.easybuy.products.repository;

import com.backtech.easybuy.products.entity.Product;
import com.backtech.easybuy.products.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepo extends JpaRepository<Review, Long> {

   List<Review> findByProduct(Product product);

   List<Review> findByProduct_Id(UUID productId);
}
