package com.finalproject.recruit.jisoo.repository;

import com.finalproject.recruit.jisoo.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {

    Optional<Mail> findTopByApplyApplyIdOrderByCreatedTimeDesc(Long applyId);

    List<Mail> findByRecruitRecruitId(Long recruitId);
}
