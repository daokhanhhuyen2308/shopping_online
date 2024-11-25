package com.dao.shopping.service;


import com.dao.shopping.dto.requests.GiftRequest;
import com.dao.shopping.dto.responses.GiftResponse;

public interface IGiftService {

    GiftResponse createANewGift(GiftRequest request);
}
