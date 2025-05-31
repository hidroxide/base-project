package com.ecommerce.fashionbackend.user.dto.response;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;

    private String street;

    private String ward;

    private String district;

    private String province;

    private boolean isDefault;
}
