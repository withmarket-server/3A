package com.market.aaa.service;

import com.market.aaa.entity.Members;
import com.market.aaa.repository.MembersCustomRepository;
import com.market.aaa.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MembersCustomRepository membersCustomRepository;

    private final MembersRepository membersRepository;

    public Members findMemberByMemberId(String memberId) {
        return membersCustomRepository.findByMemberId(memberId)
                .orElseThrow();
    }

    public Boolean existsByMemberId(String memberId) {
        return membersRepository.existsByMemberId(memberId);
    }
}
