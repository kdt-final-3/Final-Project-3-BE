package com.finalproject.recruit.entity.apply;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.MilitaryCategory;
import com.finalproject.recruit.parameter.MilitaryClass;
import com.finalproject.recruit.parameter.MilitaryDivision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Military {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "military_id")
    private Long militaryId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "military_start")
    private LocalDateTime militaryStart;

    @Column(name = "military_end")
    private LocalDateTime militaryEnd;

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
