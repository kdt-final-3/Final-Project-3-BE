package com.finalproject.recruit.entity.apply;

import com.finalproject.recruit.entity.Apply;
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
public class Certificate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long certificateId;

    @JoinColumn(name = "apply_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Apply apply;

    @Column(name = "certificate_name")
    private String certificateName;

    @Column(name = "certificate_date")
    private LocalDateTime certificateDate;

    @Column(name = "certificate_publisher")
    private String certificatePublisher;
}
