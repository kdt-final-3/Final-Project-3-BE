package com.finalproject.recruit.entity.apply;


import com.finalproject.recruit.entity.Apply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Language {

    @Id
    @Column(name = "language_id")
    private Long languageId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "language_name")
    private String languageName;

    @Column(name = "language_skill")
    private String languageSkill;

    @Column(name = "language_level")
    private String languageLevel;

    @Column(name = "language_date")
    private String languageDate;
}
