package com.ecommerce.fashionbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    private String author;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;
}
