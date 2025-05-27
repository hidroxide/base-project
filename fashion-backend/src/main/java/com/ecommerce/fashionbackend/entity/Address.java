package com.ecommerce.fashionbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String ward;

    private String district;

    private String province;

    @OneToOne(mappedBy = "address")
    private User user;
}
