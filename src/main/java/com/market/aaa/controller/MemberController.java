package com.market.aaa.controller;

import com.market.aaa.entity.Members;
import com.market.aaa.payload.request.LoginRequest;
import com.market.aaa.payload.response.TokenResponse;
import com.market.aaa.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public Members findMemberByMemberId(@PathVariable String memberId) {
        return memberService.findMemberByMemberId(memberId);
    }

    @GetMapping("/exists/{memberId}")
    public Boolean existsByMemberId(@PathVariable String memberId) {
        return memberService.existsByMemberId(memberId);
    }
}
