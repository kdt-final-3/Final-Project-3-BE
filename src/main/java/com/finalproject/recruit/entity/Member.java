package com.finalproject.recruit.entity;

import com.finalproject.recruit.dto.member.MemberReqDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
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

    /**
     * 탈퇴여부
     */
    @Column(name = "member_delete")
    private boolean memberDelete;


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<String> roles = new ArrayList<>();
//        roles.add("MEMBER");
//        return roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getUsername() {
//        return memberEmail;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }

    public void resetPassword(String password) {
        this.password = password;
    }

    public void updateMemberInfo(MemberReqDTO.Edit edit) {
        this.password = edit.getPassword() == null || edit.getPassword().equals("")? password:edit.getPassword();
        this.memberPhone = edit.getMemberPhone() == null || edit.getMemberPhone().equals("")? memberPhone: edit.getMemberPhone();
        this.ceoName = edit.getCeoName() == null || edit.getCeoName().equals("")? ceoName:edit.getCeoName();
    }

    public void dropMember() {
        this.memberDelete = true;
    }
}
