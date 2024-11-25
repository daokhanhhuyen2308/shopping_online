package com.dao.shopping.controller;


import com.dao.shopping.dto.requests.GiftRequest;
import com.dao.shopping.dto.responses.GiftResponse;
import com.dao.shopping.service.IGiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/gift")
public class GiftController {

    private final IGiftService giftService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<GiftResponse> createANewGift(@RequestBody GiftRequest request){
        return ResponseEntity.ok(giftService.createANewGift(request));
    }

}
