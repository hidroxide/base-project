package com.ecommerce.fashionbackend.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("ACTIVE")
    ACTIVE,
    @JsonProperty("INACTIVE")
    INACTIVE,
    @JsonProperty("BLOCKED")
    BLOCKED
}
