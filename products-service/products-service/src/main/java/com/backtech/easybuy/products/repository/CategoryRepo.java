package com.backtech.easybuy.products.repository;

import com.backtech.easybuy.products.entity.Category;
import com.backtech.easybuy.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category,Long> {


    @Query("SELECT c FROM Category c JOIN c.products p WHERE p.id = :productId")
    List<Category> findByProductId(@Param("productId") UUID productId);


}
