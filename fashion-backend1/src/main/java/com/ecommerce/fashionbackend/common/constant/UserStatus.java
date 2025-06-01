package com.ecommerce.fashionbackend.common.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("ACTIVE")
    ACTIVE,
    @JsonProperty("BLOCKED")
    BLOCKED
}