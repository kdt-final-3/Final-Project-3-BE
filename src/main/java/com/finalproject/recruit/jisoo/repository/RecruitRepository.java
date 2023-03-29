package com.finalproject.recruit.jisoo.repository;

import com.finalproject.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    Optional<Recruit> findTopByRecruitDeleteIsFalseAndMemberMemberEmailOrderByCreatedTimeDesc(String email);

    Optional<Recruit> findByRecruitIdAndRecruitDeleteIsFalse(Long recruitId);

    List<Recruit> findByMemberMemberEmail(String memberEmail);

}
