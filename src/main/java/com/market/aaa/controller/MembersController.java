package com.market.aaa.controller;

import com.market.aaa.dto.MemberLoginRequestDto;
import com.market.aaa.dto.TokenInfoDto;
import com.market.aaa.service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MembersController {

    private final MembersService membersService;

    @PostMapping("/login")
    public TokenInfoDto login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String memberId = memberLoginRequestDto.getMemberId();
        String password = memberLoginRequestDto.getPassword();
        return membersService.login(memberId, password);
    }
}
