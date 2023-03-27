package com.finalproject.recruit.repository;

import com.finalproject.recruit.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByMemberEmail(String email);

    Optional<Member> findByMemberEmail(String email);
}
