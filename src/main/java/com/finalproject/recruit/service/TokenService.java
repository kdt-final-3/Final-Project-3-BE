package com.finalproject.recruit.service;

import com.finalproject.recruit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final MemberRepository memberRepository;
    public boolean isMemberValid(String memberEmail){
        if(memberRepository.findByMemberEmail(memberEmail).isEmpty()){
            return false;
        }return true;
    }
}
