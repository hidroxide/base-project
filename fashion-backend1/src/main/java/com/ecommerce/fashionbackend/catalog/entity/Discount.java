package com.ecommerce.fashionbackend.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double percent;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        boolean afterStart = (startAt == null || now.isAfter(startAt));
        boolean beforeEnd = (endAt == null || now.isBefore(endAt));
        return afterStart && beforeEnd;
    }
}
