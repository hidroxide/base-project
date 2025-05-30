package com.ecommerce.fashionbackend.dto.request;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;

    private String description;

    private String imageUrl;

    private Boolean status;
}
