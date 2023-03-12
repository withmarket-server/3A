package com.market.aaa.config.security.service;

import com.market.aaa.entity.Members;
import com.market.aaa.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MembersRepository membersRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return membersRepository.findByMemberId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 CustomUserDetails 객체로 만들어서 리턴
    private CustomUserDetails createUserDetails(Members member) {
        return CustomUser.builder()
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .company(member.getCompany())
                .roles(member.getRoles())
                .loginFailCount(member.getLoginFailCount())
                .build();
    }
}
