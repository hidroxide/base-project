package com.ecommerce.fashionbackend.user.dto.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String street;

    private String ward;

    private String district;

    private String province;
}
