package com.finalproject.recruit.repository;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.ApplyProcedure;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    List<Apply> findByRecruitRecruitIdAndFailApplyIsTrue(Long recruitId);

    List<Apply> findByRecruitRecruitId(Long recruitId);

    Optional<Apply> findByApplyId(Long applyId);

    List<Apply> findByRecruitRecruitIdAndApplyDeleteIsTrue(Long recruitId);

    boolean existsApplyByApplyEmailAndRecruitRecruitId(String email,Long recruitId);

    Optional<Apply> findByApplyEmail(String email);

    List<Apply> findByRecruit_RecruitIdAndApplyProcedure(Long recruitId, ApplyProcedure applyProcedure);

    @Query("select count(a) from Apply a where a.recruit.recruitId = :id and a.applyDelete = false")
    Long countApplicantByRecruitId(@Param("id") Long id);

    @Query("select count(a) from Apply a where a.recruit.recruitId = :id and DATE_FORMAT(a.createdTime, '%Y-%m-%d') = DATE_FORMAT(:today, '%Y-%m-%d') and a.applyDelete = false")
    Long countApplicantByRecruitIdAndCreatedTime(@Param("id") Long id, @Param("today") LocalDate today);

    @EntityGraph(attributePaths = {"career","education","language","military","certificate","activities","awards"})
    Optional<Apply> findJoinByApplyId(Long applyId);
}
