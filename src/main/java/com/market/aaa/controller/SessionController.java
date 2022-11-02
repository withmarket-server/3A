package com.market.aaa.controller;

import com.market.aaa.DTO.Member;
import com.market.aaa.DTO.SessionDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Log4j2
@RestController
public class SessionController {

    @GetMapping("/session")
    public SessionDTO hasSession(@AuthenticationPrincipal Member member, HttpSession httpSession) {
        return member.getUserName() != null ?
                        SessionDTO.builder()
                            .userName(member.getUserName())
                            .sessionId(httpSession.getId())
                            .build() : null;
    }



}
