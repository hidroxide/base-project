package com.ecommerce.fashionbackend.user.mapper;

import com.ecommerce.fashionbackend.user.dto.request.AddressRequest;
import com.ecommerce.fashionbackend.user.dto.response.AddressResponse;
import com.ecommerce.fashionbackend.user.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest addressRequest);
    void updateAddress(@MappingTarget Address address, AddressRequest addressRequest);
    AddressResponse toAddressResponse(Address address);
}
