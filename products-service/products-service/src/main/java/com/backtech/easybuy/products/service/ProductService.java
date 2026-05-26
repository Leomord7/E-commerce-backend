package com.backtech.easybuy.products.service;

import com.backtech.easybuy.products.dto.PagedResponse;
import com.backtech.easybuy.products.dto.ProductDto;
import com.backtech.easybuy.products.dto.ReviewDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
   //fetch all products
    PagedResponse<ProductDto> getAllProducts(int page, int size);
// fetch product by id
    ProductDto getProductById(UUID productId);
// fetch products by category id
    PagedResponse<ProductDto> getProductsByCategoryId(Long categoryId, int page, int size);
// create product
    ProductDto createProduct(ProductDto productDto);
// update product
    ProductDto updateProduct(UUID productId, ProductDto productDto);
// delete product
    void deleteProduct(UUID productId);
// add category to product
    ProductDto addCategoryToProduct(UUID productId, Long categoryId);
//  remove category from product
    ProductDto removeCategoryFromProduct(UUID productId, Long categoryId);
// add review to product
    ReviewDto addReviewToProduct(UUID productId, ReviewDto reviewDto);
// add product image
    ProductDto addProductImages(UUID productId, List<MultipartFile> files);
// get product images
    List<String> getProductImages(UUID productId);
}

