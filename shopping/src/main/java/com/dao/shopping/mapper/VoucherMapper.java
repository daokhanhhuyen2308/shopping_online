package com.dao.shopping.mapper;


import com.dao.shopping.dto.responses.VoucherResponse;
import com.dao.shopping.entity.VoucherEntity;

public class VoucherMapper {
    public static VoucherResponse mapVoucherToVoucherResponse(VoucherEntity voucherEntity){
        VoucherResponse voucherResponse = new VoucherResponse();
        voucherResponse.setId(voucherResponse.getId());
        voucherResponse.setCode(voucherEntity.getCode());
        voucherResponse.setDescription(voucherEntity.getDescription());
        voucherResponse.setDiscountPercentage(voucherResponse.getDiscountPercentage());
        voucherResponse.setDiscountAmount(voucherEntity.getDiscountAmount());
        voucherResponse.setStartDate(voucherEntity.getStartDate());
        return voucherResponse;
    }

}
