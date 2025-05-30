package com.ecommerce.fashionbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private boolean isMainImage;

    private Integer position;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
