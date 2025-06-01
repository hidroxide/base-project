package com.ecommerce.fashionbackend.user.repository;

import com.ecommerce.fashionbackend.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}