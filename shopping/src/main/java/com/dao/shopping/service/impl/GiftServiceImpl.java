package com.dao.shopping.service.impl;


import com.dao.shopping.dto.requests.GiftRequest;
import com.dao.shopping.dto.responses.GiftResponse;
import com.dao.shopping.entity.GiftEntity;
import com.dao.shopping.entity.VoucherEntity;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.repository.GiftRepository;
import com.dao.shopping.repository.VoucherRepository;
import com.dao.shopping.service.IGiftService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GiftServiceImpl implements IGiftService {

    GiftRepository giftRepository;

    VoucherRepository voucherRepository;

    ModelMapper modelMapper;

    @Override
    public GiftResponse createANewGift(GiftRequest request) {

        VoucherEntity voucherEntity = voucherRepository.findById(request.getVoucherId())
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Voucher's id not found"));

        GiftEntity giftEntity = modelMapper.map(request, GiftEntity.class);
        giftEntity.setVoucher(voucherEntity);

        giftRepository.save(giftEntity);

        GiftResponse response = modelMapper.map(giftEntity, GiftResponse.class);
        response.setVoucherId(voucherEntity.getId());

        return response;
    }
}
