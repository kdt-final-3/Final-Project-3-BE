package com.finalproject.recruit.repository;

import com.finalproject.recruit.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {

    Optional<Mail> findTopByApplyApplyIdOrderByCreatedTimeDesc(Long applyId);

    List<Mail> findByRecruitRecruitId(Long recruitId);
}
