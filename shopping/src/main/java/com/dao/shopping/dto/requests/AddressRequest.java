package com.dao.shopping.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {
    private String houseNumber;
    private String street;
    private String district;
    private String city;
    private String zip;
    private String country;
}
