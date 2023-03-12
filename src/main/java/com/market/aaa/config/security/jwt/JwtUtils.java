package com.market.aaa.config.security.jwt;

import com.market.aaa.config.security.service.CustomUser;
import com.market.aaa.config.security.service.CustomUserDetails;
import com.market.aaa.entity.Members;
import com.market.aaa.payload.response.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.market.aaa.config.constant.Constants.*;

@Component
@Log4j2
public class JwtUtils {

    private final Key key;

    public JwtUtils(@Value("${jwt.secret}") String secretKey) {
        log.warn("secretKey = {}", secretKey);
//        byte[] keyBytes = Base64Utils.decodeFromUrlSafeString(String.valueOf(secretKey));
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenResponse generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date accessTokenExpiresIn = Date.from(now.plus(Duration.ofMillis(ACCESS_TOKEN_EXPIRE_TIME)));
        Date refreshTokenExpiresIn = Date.from(now.plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME)));

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(JWT_CLAIMS_ROLE, authorities)
                .claim(JWT_CLAIMS_COMPANY, customUserDetails.getCompany())
                .setIssuedAt(issuedAt)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .setIssuedAt(issuedAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenResponse.builder()
                .grantType(JWT_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(JWT_CLAIMS_ROLE) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        String[] rolesArray = claims.get(JWT_CLAIMS_ROLE, String.class).split(",");
        List<String> roles = Arrays.asList(rolesArray);

        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 클레임에서 회사 정보 가져오기
        String company = claims.get(JWT_CLAIMS_COMPANY, String.class);

        // UserDetails 객체를 만들어서 Authentication 리턴
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
        CustomUserDetails principal = new CustomUser(claims.getSubject(), "", roles, company, 0);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public TokenResponse refreshToken(Members members){
        String authorities = String.join(",", members.getRoles());

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date accessTokenExpiresIn = Date.from(now.plus(Duration.ofMillis(ACCESS_TOKEN_EXPIRE_TIME)));
        Date refreshTokenExpiresIn = Date.from(now.plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME)));

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(members.getMemberId())
                .claim(JWT_CLAIMS_ROLE, authorities)
                .claim(JWT_CLAIMS_COMPANY, members.getCompany())
                .setIssuedAt(issuedAt)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .setIssuedAt(issuedAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenResponse.builder()
                .grantType(JWT_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
