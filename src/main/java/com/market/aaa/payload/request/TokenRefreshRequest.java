package com.market.aaa.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {

  @NotBlank
  private String refreshToken;
}
