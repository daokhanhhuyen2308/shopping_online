package com.dao.shopping.controller;


import com.dao.shopping.dto.requests.SaleRequest;
import com.dao.shopping.dto.responses.SaleResponse;
import com.dao.shopping.service.ISaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {

    private final ISaleService iSaleService;

    @PostMapping("/create-sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleResponse> createSale(@RequestBody SaleRequest request){
        return ResponseEntity.ok(iSaleService.createSale(request));
    }

    //flash-sale or deal-hot, deal-now
    @GetMapping("/{type}")
    public ResponseEntity<SaleResponse> getProductsBySaleType(@PathVariable("type") String type){
        SaleResponse saleResponse = iSaleService.getProductsBySaleType(type);

        if (saleResponse != null) {
            return ResponseEntity.ok(saleResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }


}
