package com.ecommerce.fashionbackend.mapper;

import com.ecommerce.fashionbackend.dto.request.AddressRequest;
import com.ecommerce.fashionbackend.dto.response.AddressResponse;
import com.ecommerce.fashionbackend.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest addressRequest);
    void updateAddress(@MappingTarget Address address, AddressRequest addressRequest);
    AddressResponse toAddressResponse(Address address);
}
