package com.ecommerce.fashionbackend.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntrospectResponse {
    private boolean isValid;
}
