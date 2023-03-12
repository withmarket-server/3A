package com.market.aaa.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String memberId;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
}
