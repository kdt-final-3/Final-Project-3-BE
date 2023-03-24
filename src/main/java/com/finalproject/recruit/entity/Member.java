package com.finalproject.recruit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class Member {

    /**
     * 기업회원 아이디
     */
    @Id
    @Column(name = "member_email")
    private String memberEmail;

    /**
     * 기업 비밀번호
     */
    @Column(name = "password")
    private String password;

    /**
     * 담당자 전화번호
     */
    @Column(name = "member_phone")
    private String memberPhone;

    /**
     * 사업자 등록번호
     */
    @Column(name = "company_num")
    private String companyNum;

    /**
     * 기업명
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 담당자명
     */
    @Column(name = "ceo_name")
    private String ceoName;


}
