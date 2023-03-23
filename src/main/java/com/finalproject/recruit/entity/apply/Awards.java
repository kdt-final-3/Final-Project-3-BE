package com.finalproject.recruit.entity.apply;

import com.finalproject.recruit.entity.Apply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Awards {

    @Id
    @Column(name = "awards_id")
    private Long awardsId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "awards_name")
    private String awardsName;

    @Column(name = "awards_date")
    private Date awardsDate;

    @Column(name = "awards_company")
    private String awardsCompany;
}
