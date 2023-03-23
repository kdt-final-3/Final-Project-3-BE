package com.finalproject.recruit.entity.apply;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.EduStatus;
import com.finalproject.recruit.parameter.EduYear;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Education {


    @Id
    @Column(name = "edu_id")
    private Long eduId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "edu_name")
    private String eduName;

    @Enumerated(EnumType.STRING)
    @Column(name = "edu_year")
    private EduYear eduYear;

    @Column(name = "edu_major")
    private String eduMajor;

    @Enumerated(EnumType.STRING)
    @Column(name = "edu_status")
    private EduStatus eduStatus;

    @Column(name = "edu_start")
    private LocalDateTime eduStart;

    @Column(name = "edu_end")
    private LocalDateTime eduEnd;

}
