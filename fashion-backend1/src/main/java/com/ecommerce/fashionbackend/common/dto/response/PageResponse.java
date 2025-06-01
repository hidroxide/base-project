package com.ecommerce.fashionbackend.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private int pageNo;

    private int pageSize;

    private int totalPage;

    private List<T> items;
}