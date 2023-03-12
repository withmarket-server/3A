package com.market.aaa.config.security.service;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class CustomUser implements CustomUserDetails {
    private String memberId;
    private String password;
    private List<String> roles;
    private String company;

    private int loginFailCount;

    public CustomUser(String memberId, String password, List<String> roles, String company, int loginFailCount) {
        this.memberId = memberId;
        this.password = password;
        this.roles = roles;
        this.company = company;
        this.loginFailCount = loginFailCount;
    }

    // 커스텀 필드를 구현
//    @Override
//    public String getCompany() {
//        return company;
//    }

    @Override
    public int loginFailCount() {
        return loginFailCount;
    }

    // UserDetails의 메소드를 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

