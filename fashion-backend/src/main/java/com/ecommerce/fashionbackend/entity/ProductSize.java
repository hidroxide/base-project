package com.ecommerce.fashionbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_size")
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
