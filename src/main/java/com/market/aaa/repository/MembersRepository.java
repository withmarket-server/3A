package com.market.aaa.repository;

import com.market.aaa.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, String> {
    Optional<Members> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

    Optional<Members> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("UPDATE Members m SET m.refreshToken = :refreshToken WHERE m.memberId = :memberId")
    void updateRefreshToken(@Param("memberId") String memberId, @Param("refreshToken") String refreshToken);

    @Modifying
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    // 해당 메서드는 새로운 트랜잭션에서 실행되므로, 해당 트랜잭션에서 변경된 내용은 상위 트랜잭션의 롤백 대상이 아니게 되는 옵션
    @Query("UPDATE Members m SET m.loginFailCount = m.loginFailCount +1 WHERE m.memberId = :memberId")
    void updateLoginFailCount(@Param("memberId") String memberId);
}
