package com.dao.shopping.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandDTORequest {
    private String brandName;
}
