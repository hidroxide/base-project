package com.ecommerce.fashionbackend.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TokenType {
    @JsonProperty("ACCESS")
    ACCESS,
    @JsonProperty("REFRESH")
    REFRESH
}
