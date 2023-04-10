package com.finalproject.recruit.dto.applymanage;

import com.finalproject.recruit.entity.apply.Certificate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponseDTO { //지원서 엔티티와 자격증 엔티티를 1대다로 변경 시에 확장 위한 DTO

    String certificateName;
    LocalDateTime certificateDate;
    String certificatePublisher;

    public CertificateResponseDTO(Certificate certificate) {
        this.certificateName = certificate.getCertificateName();
        this.certificateDate = certificate.getCertificateDate();
        this.certificatePublisher = certificate.getCertificatePublisher();
    }
}
