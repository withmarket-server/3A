package com.market.aaa.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberLoginRequestDto {
    private String memberId;
    private String password;
}
