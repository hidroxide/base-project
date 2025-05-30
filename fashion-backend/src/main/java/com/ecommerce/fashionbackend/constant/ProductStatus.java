package com.ecommerce.fashionbackend.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProductStatus {
    @JsonProperty("NEW")
    NEW,
    @JsonProperty("HOT")
    HOT,
    @JsonProperty("SALE")
    SALE,
    @JsonProperty("NORMAL")
    NORMAL,
    @JsonProperty("OUT_OF_STOCK")
    OUT_OF_STOCK
}
