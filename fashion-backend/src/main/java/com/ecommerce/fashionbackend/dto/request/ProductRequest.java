package com.ecommerce.fashionbackend.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    private String name;

    private String description;

    private Double price;

    private List<SizeRequest> sizes;

    private List<ImageRequest> images;
}
