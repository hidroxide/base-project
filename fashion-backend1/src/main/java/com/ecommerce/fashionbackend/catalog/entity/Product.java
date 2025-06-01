package com.ecommerce.fashionbackend.catalog.entity;

import com.ecommerce.fashionbackend.common.constant.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    private ProductStatus productStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSize> sizes;

    public Double getFinalPrice() {
        if (price == null) return 0.0;
        if (discount != null && discount.isActive() && discount.getPercent() != null) {
            return price * (1 - discount.getPercent()/100);
        }
        return price;
    }
}
