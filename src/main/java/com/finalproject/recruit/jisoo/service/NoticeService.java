package com.finalproject.recruit.jisoo.service;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.jisoo.Mail;
import com.finalproject.recruit.jisoo.dto.*;
import com.finalproject.recruit.jisoo.parameter.NoticeStep;
import com.finalproject.recruit.jisoo.repository.ApplyRepository;
import com.finalproject.recruit.jisoo.repository.MailRepository;
import com.finalproject.recruit.jisoo.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;
    private final MailRepository mailRepository;

    public List<NoticeRecruitsRes> recruitList(String memberEmail) {
        return recruitRepository.findByMemberMemberEmail(memberEmail).stream()
                .map(NoticeRecruitsRes::fromRecruit)
                .collect(Collectors.toList());
    }

    public List<ApplicantsRes> applicantList(Long recruitId) {

        return applyRepository.findByRecruitRecruitId(recruitId)
                .stream()
                .map(apply -> ApplicantsRes.fromApply(
                        apply,
                        mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                .map(Mail::getCreatedTime)
                                .orElse(null)))
                .collect(Collectors.toList());

    }

    public List<MessageHistoryRes> messageHistory(Long recruitId) {
        return mailRepository.findByRecruitRecruitId(recruitId)
                .stream()
                .map(mail -> MessageHistoryRes.fromEntity(
                        mail,
                        mail.getApply()
                ))
                .collect(Collectors.toList());
    }

    public String sendEmail(EmailReq emailReq) {
        try {
            Apply apply = applyRepository.findByApplyId(emailReq.getApplyId()).orElseThrow(()-> new IllegalArgumentException("지원자가 존재하지 않습니다."));
            Recruit recruit = recruitRepository.findByRecruitIdAndRecruitDeleteIsFalse(emailReq.getRecruitId()).orElseThrow(()-> new IllegalArgumentException("해당 채용공고가 존재하지 않습니다."));
            String message=emailReq.getMailContent(); //보낼 메시지
            String recipient = apply.getApplyEmail(); //받는 사람 (이메일)
            if (emailReq.getNoticeStep().equals(NoticeStep.면접제안)) {
                message+="면접 날짜는 "+ emailReq.getInterviewDate()+"입니다.\n"+
                    "잘 준비하셔서 좋은 성과 있으시길 바랍니다.\n";
            }
            message+="감사합니다. ";
            //이메일 전송!!

            //apply 배열일경우

            //이메일 전송 내역 저장
            Mail mail = Mail.builder()
                    .recruit(recruit)
                    .apply(apply)
                    .mailContent(message)
                    .noticeStep(emailReq.getNoticeStep())
                    .build();
            mailRepository.save(mail);

            return "success";

        } catch (Exception e){
            return "fail";
        }
    }

    public String selectStep(SelectStepReq selectStepReq) {
        Recruit recruit = recruitRepository.findByRecruitIdAndRecruitDeleteIsFalse(selectStepReq.getRecruitId())
                .orElseThrow(() -> new IllegalArgumentException("해당 채용공고가 존재하지 않습니다."));

        String message = "안녕하세요. "+ recruit.getMember().getCompanyName()+"입니다.\n" +
                "저희 "+ recruit.getRecruitTitle()+"에 관심을 가지고 지원해 주셔서 감사합니다.\n";
        message+=selectStepReq.getNoticeStep().getMessage();

        return message;
    }
}
