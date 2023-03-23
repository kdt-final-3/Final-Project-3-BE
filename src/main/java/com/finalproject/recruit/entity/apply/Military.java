package com.finalproject.recruit.entity.apply;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.MilitaryCategory;
import com.finalproject.recruit.parameter.MilitaryClass;
import com.finalproject.recruit.parameter.MilitaryDivision;
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
public class Military {

    @Id
    @Column(name = "military_id")
    private Long militaryId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "military_start")
    private Date militaryStart;

    @Column(name = "military_end")
    private Date militaryEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "military_division")
    private MilitaryDivision militaryDivision;

    @Enumerated(EnumType.STRING)
    @Column(name = "military_category")
    private MilitaryCategory militaryCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "military_class")
    private MilitaryClass militaryClass;

    @Column(name = "military_exemption")
    private String militaryExemption;

}