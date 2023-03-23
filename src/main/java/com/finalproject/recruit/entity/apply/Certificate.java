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
public class Certificate {

    @Id
    @Column(name = "certificate_id")
    private Long certificateId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "certificate_name")
    private String certificateName;

    @Column(name = "certificate_date")
    private Date certificateDate;

    @Column(name = "certificate_publisher")
    private String certificatePublisher;
}
