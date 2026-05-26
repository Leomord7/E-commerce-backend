package com.backtech.easybuy.products.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity {
    @Id
    @UuidGenerator // Generate UUID automatically
    @GeneratedValue
    private UUID id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String shortDesc;
    @Column(columnDefinition = "TEXT")
    private String longDesc;
    private Double price;
    private Integer discount;
    private Boolean live = false;
    @ElementCollection(fetch = FetchType.EAGER) // Store product images as a list of strings
    private List<String> productImages = new ArrayList<>();

//    @ManyToOne
//    private Category category;
    @ManyToMany(mappedBy = "products")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


}
