package com.market.aaa.service;

import com.market.aaa.config.utils.Mapper;
import com.market.aaa.payload.request.SignupRequest;
import com.market.aaa.payload.request.TokenRefreshRequest;
import com.market.aaa.payload.response.TokenResponse;
import com.market.aaa.entity.Members;
import com.market.aaa.config.security.jwt.JwtUtils;
import com.market.aaa.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    private final MembersRepository membersRepository;

    private final PasswordEncoder passwordEncoder;

    private final Mapper mapper;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtUtils jwtUtils;

    @Transactional
    public TokenResponse signin(String memberId, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);

        // 2. 사용자 비밀번호 체크
        // authenticate 매서드가 실행될 때 CustomMemberDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenResponse = jwtUtils.generateToken(authentication);
        membersRepository.updateRefreshToken(memberId, tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    public Members signup(SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        return membersRepository.save(mapper.map(signupRequest, Members.class));
    }

    @Transactional
    public TokenResponse refreshToken(TokenRefreshRequest request) {

        jwtUtils.validateToken(request.getRefreshToken());

        Members members = membersRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        TokenResponse tokenResponse = jwtUtils.refreshToken(members);

        membersRepository.updateRefreshToken(members.getMemberId(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }


}
