package com.backtech.easybuy.products.service.impl;

import com.backtech.easybuy.products.dto.CategoryDto;
import com.backtech.easybuy.products.dto.PagedResponse;
import com.backtech.easybuy.products.dto.ProductDto;
import com.backtech.easybuy.products.dto.ReviewDto;
import com.backtech.easybuy.products.entity.Category;
import com.backtech.easybuy.products.entity.Product;
import com.backtech.easybuy.products.entity.Review;
import com.backtech.easybuy.products.exception.ResourceNotFoundException;
import com.backtech.easybuy.products.repository.CategoryRepo;
import com.backtech.easybuy.products.repository.ProductRepo;
import com.backtech.easybuy.products.repository.ReviewRepo;
import com.backtech.easybuy.products.service.ImageStorageService;
import com.backtech.easybuy.products.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ReviewRepo reviewRepo;
    private final ImageStorageService imageStorageService;

    public ProductServiceImpl(ProductRepo productRepo, CategoryRepo categoryRepo, ReviewRepo reviewRepo, ImageStorageService imageStorageService)
    {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.reviewRepo = reviewRepo;
        this.imageStorageService = imageStorageService;
    }



    @Override
    public PagedResponse<ProductDto> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepo.findAll(pageable);
        //method reference
        return toPagedResponse(productPage.map(this::toDto));

    }


    @Override
    public ProductDto getProductById(UUID productId) {
        return toDto(findProduct(productId));
    }



    @Override
    public PagedResponse<ProductDto> getProductsByCategoryId(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepo.findByCategories_Id(categoryId, pageable);

        //conversations
        return toPagedResponse(productPage.map(this::toDto));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = new Product();
        applyBasicFields(product, productDto);
        List<Category> categories = resolveCategories(productDto.getCategories());
        product.setCategories(categories);
        Product savedProduct = productRepo.save(product);
        return toDto(savedProduct);
    }




    @Override
    public ProductDto updateProduct(UUID productId, ProductDto productDto) {
        Product product = findProduct(productId);

            applyBasicFields(product, productDto);
            if(productDto.getCategories() !=null){
                List<Category> categories = resolveCategories(productDto.getCategories());
                product.setCategories(categories);
                // update
               Product saveProduct = productRepo.save(product);
                return toDto(saveProduct);
            }
            return toDto(productRepo.save(product));

    }

    @Override
    public void deleteProduct(UUID productId) {
        Product product = findProduct(productId);
        productRepo.delete(product);
    }

    @Override
    public ProductDto addCategoryToProduct(UUID productId, Long categoryId) {
        Product product = findProduct(productId);
        Category category = findCategory(categoryId);
        if(!product.getCategories().contains(category)){
            product.getCategories().add(category);
        }
        if(!category.getProducts().contains(product)){
            category.getProducts().add(product);
        }
        categoryRepo.save(category);
        Product saveProduct = productRepo.save(product);
        return toDto(saveProduct);

    }

    @Override
    public ProductDto removeCategoryFromProduct(UUID productId, Long categoryId) {
        Product product = findProduct(productId);
        Category category = findCategory(categoryId);
        // remove 2 reference
        product.getCategories().remove(category);
        category.getProducts().remove(product);
        categoryRepo.save(category);

        return toDto(productRepo.save(product));
    }

    @Override
    public ReviewDto addReviewToProduct(UUID productId, ReviewDto reviewDto) {
        Product product = findProduct(productId);
        Review review = new Review();
        review.setTitle(review.getTitle());
        review.setRating(review.getRating());
        review.setComment(review.getComment());
        review.setProduct(product);
        return toReviewDto(reviewRepo.save(review));
    }



    @Override
    public ProductDto addProductImages(UUID productId, List<MultipartFile> files) {
        Product product = findProduct(productId);
        List<String> listImageUrls = uploadImages(files);
        product.setProductImages(listImageUrls);
        Product saveProduct = productRepo.save(product);
        return toDto(saveProduct);
    }



    @Override
    public List<String> getProductImages(UUID productId) {
        Product product = findProduct(productId);
        return product.getProductImages() == null ? new ArrayList<>() : new ArrayList<>(product.getProductImages());
    }

    /// new method

    private ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setShortDesc(product.getShortDesc());
        dto.setLongDesc(product.getLongDesc());
        dto.setPrice(product.getPrice());
        dto.setDiscount(product.getDiscount());
        dto.setLive(product.getLive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setProductImages(product.getProductImages() == null ? new ArrayList<>() : new ArrayList<>(product.getProductImages()));
        dto.setCategories(product.getCategories() == null ? new ArrayList<>() : product.getCategories().stream().map(this::toCategoryDtoShallow).collect(Collectors.toList()));
        dto.setReviews(product.getReviews() == null ? new ArrayList<>() : product.getReviews().stream().map(this::toReviewDtoShallow).collect(Collectors.toList()));
        return dto;
    }
    private CategoryDto toCategoryDtoShallow(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        dto.setProducts(new ArrayList<>());
        return dto;
    }

    private ReviewDto toReviewDtoShallow(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setTitle(review.getTitle());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setProduct(null);
        return dto;
    }

    private PagedResponse<ProductDto> toPagedResponse(Page<ProductDto> page) {
        return new PagedResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast(), page.isFirst());
    }
    private Product findProduct(UUID productId) {
         return productRepo.findById(productId).
                 orElseThrow(() -> new ResourceNotFoundException("Product not found : " +productId));
    }

    private void applyBasicFields(Product product, ProductDto productDto) {
        product.setTitle(productDto.getTitle());
        product.setShortDesc(productDto.getShortDesc());
        product.setLongDesc(productDto.getLongDesc());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        if(productDto.getLive() !=null){
            product.setLive(productDto.getLive());
        }
        if(productDto.getProductImages() !=null){
            product.setProductImages(productDto.getProductImages());
        }


    }
    private List<Category> resolveCategories(List<CategoryDto> categories) {
        if(categories == null){
            return new ArrayList<>();
        }
       List<Category> categoriesList = new ArrayList<>();
       for(CategoryDto categoryDto : categories){
           if(categoryDto.getId() == null){
               Category category = new Category();
               category.setTitle(categoryDto.getTitle());
               categoriesList.add(categoryRepo.save(category));
           }
           else{
               categoriesList.add(findCategory(categoryDto.getId()));
           }

       }
       return categoriesList;
    }

    private Category findCategory(Long id) {
       return categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found : " + id));
    }

    private ReviewDto toReviewDto(Review save) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(save.getId());
        reviewDto.setTitle(save.getTitle());
        reviewDto.setRating(save.getRating());
        reviewDto.setComment(save.getComment());
        return reviewDto;
    }
    private List<String> uploadImages(List<MultipartFile> files) {
        List<String> images = new ArrayList<>();
        for(MultipartFile imageFile : files){

            images.add(imageStorageService.uploadImage(imageFile));
        }
        return images;
    }
}
