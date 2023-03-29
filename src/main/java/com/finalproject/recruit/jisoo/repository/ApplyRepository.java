package com.finalproject.recruit.jisoo.repository;

import com.finalproject.recruit.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    List<Apply> findByRecruitRecruitIdAndFailApplyIsTrue(Long recruitId);

    List<Apply> findByRecruitRecruitId(Long recruitId);

    Optional<Apply> findByApplyId(Long applyId);

    List<Apply> findByRecruitRecruitIdAndApplyDeleteIsTrue(Long recruitId);

    boolean existsApplyByApplyEmailAndRecruitRecruitId(String email,Long recruitId);

}
