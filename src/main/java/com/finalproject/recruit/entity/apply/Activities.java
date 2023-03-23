package com.finalproject.recruit.entity.apply;

import com.finalproject.recruit.entity.Apply;
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
public class Activities {

    @Id
    @Column(name = "activities_id")
    private Long activitiesId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "activities_title")
    private String activitiesTitle;

    @Column(name = "activities_content")
    private String activitiesContent;

    @Column(name = "activities_start")
    private LocalDateTime activitiesStart;

    @Column(name = "activities_end")
    private LocalDateTime activitiesEnd;

}
