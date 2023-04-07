package com.finalproject.recruit.repository;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.ApplyProcedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    Page<Apply> findByRecruitRecruitIdAndFailApplyIsTrue(Long recruitId, Pageable pageable);

    Page<Apply> findByApplyNameAndRecruitRecruitIdAndFailApplyIsTrue(String applyName, Long recruitId, Pageable pageable);

    List<Apply> findByRecruitRecruitId(Long recruitId);

    Page<Apply> findByRecruitRecruitId(Long recruitId, Pageable pageable);

    Optional<Apply> findByApplyId(Long applyId);

    Page<Apply> findByRecruitRecruitIdAndApplyDeleteIsTrue(Long recruitId, Pageable pageable);

    boolean existsApplyByApplyEmailAndRecruitRecruitId(String email,Long recruitId);

    Optional<Apply> findByApplyEmail(String email);

    List<Apply> findByApplyNameAndRecruitRecruitId(String applyName, Long recruitId);

    @EntityGraph(attributePaths = {"career","education","language","military","certificate","activities","awards"})
    List<Apply> findByRecruit_RecruitIdAndApplyProcedure(Long recruitId, ApplyProcedure applyProcedure);

    @EntityGraph(attributePaths = {"career","education","language","military","certificate","activities","awards"})
    List<Apply> findByRecruit_RecruitId(Long recruitId);

    @Query("select count(a) from Apply a where a.recruit.recruitId = :id and a.applyDelete = false")
    Long countApplicantByRecruitId(@Param("id") Long id);

    @Query("select count(a) from Apply a where a.recruit.recruitId = :id and DATE_FORMAT(a.createdTime, '%Y-%m-%d') = DATE_FORMAT(:today, '%Y-%m-%d') and a.applyDelete = false")
    Long countApplicantByRecruitIdAndCreatedTime(@Param("id") Long id, @Param("today") LocalDate today);

    @EntityGraph(attributePaths = {"career","education","language","military","certificate","activities","awards"})
    Optional<Apply> findJoinByApplyId(Long applyId);
}
