package com.dao.shopping.controller;


import com.dao.shopping.dto.requests.AuthenticationRequest;
import com.dao.shopping.dto.responses.AuthenticationResponse;
import com.dao.shopping.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private final IAuthenticationService iAuthenticationService;

    @Autowired
    public DemoController(IAuthenticationService iAuthenticationService) {
        this.iAuthenticationService = iAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginDemo(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = iAuthenticationService.authenticate(request);
        if (response != null) {
            return ResponseEntity.ok("Hello Bro! I want to throw a punch at your face");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
