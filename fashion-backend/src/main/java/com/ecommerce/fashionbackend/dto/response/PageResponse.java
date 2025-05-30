package com.ecommerce.fashionbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse<T> {
    private int pageNo;

    private int pageSize;

    private int totalPage;

    private T items;
}