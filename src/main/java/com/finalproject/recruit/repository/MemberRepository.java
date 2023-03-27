package com.finalproject.recruit.repository;

import com.finalproject.recruit.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByMemberEmail(String email);

}
