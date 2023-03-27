package com.finalproject.recruit.repository;

import com.finalproject.recruit.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByMemberEmail(String email);

    Optional<Member> findByMemberEmail(String email);

    @Modifying
    @Query(value = "UPDATE member m SET m.password =: newPassowrd WHERE m.memberEmail", nativeQuery = true)
    Integer resetPassword(@Param("memberEmail") String email, @Param("newPassword") String newPassword);
}
