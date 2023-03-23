package com.finalproject.recruit.entity.apply;


import com.finalproject.recruit.entity.Apply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Career {

    @Id
    @Column(name = "career_id")
    private Long careerId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "career_name")
    private String careerName;

    @Column(name = "career_start")
    private Date careerStart;

    @Column(name = "career_end")
    private Date careerEnd;

    @Column(name = "career_detail")
    private String careerDetail;
}
