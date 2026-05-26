package com.backtech.easybuy.products.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Short description is required")
    @Size(max = 500, message = "Short description must be less than 500 characters")
    private String shortDesc;

    @NotBlank(message = "Long description is required")
    private String longDesc;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 100, message = "Discount must be less than or equal to 100")
    private Integer discount;
    private Boolean live;
    private List<String> productImages;
    private List<CategoryDto> categories;
    private List<ReviewDto> reviews;

    private Instant createdAt;
    private Instant updatedAt;
}
