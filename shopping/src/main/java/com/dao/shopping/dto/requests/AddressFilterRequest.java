package com.dao.shopping.dto.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressFilterRequest {
    String street;
    String city;
    String state;
    String zipCode;
    String country;

}
