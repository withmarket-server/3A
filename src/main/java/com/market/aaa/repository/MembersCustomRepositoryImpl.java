package com.market.aaa.repository;

import com.market.aaa.entity.Members;
import com.market.aaa.entity.QMembers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.market.aaa.entity.QMembers.*;

@Repository
@RequiredArgsConstructor
public class MembersCustomRepositoryImpl implements MembersCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Members> findByMemberId(String memberId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(members)
                .where(members.memberId.eq(memberId))
                .fetchOne());
    }
}
