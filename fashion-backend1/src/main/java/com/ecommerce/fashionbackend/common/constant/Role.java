package com.ecommerce.fashionbackend.common.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("ADMIN")
    ADMIN,
    @JsonProperty("USER")
    USER
}