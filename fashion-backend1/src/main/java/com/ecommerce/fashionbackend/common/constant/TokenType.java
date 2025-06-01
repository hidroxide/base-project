package com.ecommerce.fashionbackend.common.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TokenType {
    @JsonProperty("ACCESS_TOKEN")
    ACCESS_TOKEN,
    @JsonProperty("REFRESH_TOKEN")
    REFRESH_TOKEN
}