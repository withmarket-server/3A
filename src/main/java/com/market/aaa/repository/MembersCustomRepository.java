package com.market.aaa.repository;

import com.market.aaa.entity.Members;

import java.util.Optional;

public interface MembersCustomRepository {
    Optional<Members> findByMemberId(String memberId);
}
