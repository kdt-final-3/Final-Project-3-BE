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
public class CertificateResponseDTO {

    String certificateName;
    LocalDateTime certificateDate;
    String certificatePublisher;

    public CertificateResponseDTO(Certificate certificate) {
        this.certificateName = certificate.getCertificateName();
        this.certificateDate = certificate.getCertificateDate();
        this.certificatePublisher = certificate.getCertificatePublisher();
    }
}
