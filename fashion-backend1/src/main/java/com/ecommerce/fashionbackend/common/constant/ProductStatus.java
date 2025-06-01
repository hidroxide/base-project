package com.ecommerce.fashionbackend.common.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProductStatus {
    @JsonProperty("ACTIVE")
    ACTIVE,
    @JsonProperty("INACTIVE")
    INACTIVE,
    @JsonProperty("SOLD_OUT")
    SOLD_OUT
}
