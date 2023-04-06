package com.finalproject.recruit.repository;

import com.finalproject.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    /*===========================
        채용폼
    ===========================*/
    Optional<Recruit> findByRecruitId(Long recruitId);
    List<Recruit> findAllByMember_MemberEmailAndRecruitTypeAndRecruitOngoing(String email, boolean type, boolean status);
    List<Recruit> findAllByMember_MemberEmailAndRecruitOngoingAndRecruitTitleContains(String email, boolean status, String title);

    /*===========================
        탈락인재 & 알람 & 지원자
    ===========================*/
    Optional<Recruit> findTopByAndMemberMemberEmailOrderByRecruitRegistedAt(String email);
    List<Recruit> findByMemberMemberEmail(String memberEmail);
}
