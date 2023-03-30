package com.finalproject.recruit.entity;

import com.finalproject.recruit.parameter.NoticeStep;
import com.finalproject.recruit.util.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Mail extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id")
    private Apply apply;

    @Column(name = "mail_content")
    private String mailContent;

    @Column(name = "notice_step")
    @Enumerated(EnumType.STRING)
    private NoticeStep noticeStep;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

}
