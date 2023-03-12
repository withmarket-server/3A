package com.market.aaa.controller;

import com.market.aaa.payload.request.SignupRequest;
import com.market.aaa.payload.request.LoginRequest;
import com.market.aaa.payload.request.TokenRefreshRequest;
import com.market.aaa.payload.response.TokenResponse;
import com.market.aaa.entity.Members;
import com.market.aaa.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public TokenResponse signin(@Valid @RequestBody LoginRequest loginRequest) {
        String memberId = loginRequest.getMemberId();
        String password = loginRequest.getPassword();
        return authService.signin(memberId, password);
    }

    @PostMapping("/signup")
    public Members signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return authService.refreshToken(request);
    }
}
