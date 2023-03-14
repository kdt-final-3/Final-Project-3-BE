package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.UserLoginRes;
import com.finalproject.recruit.dto.UserSignupRes;
import com.finalproject.recruit.entity.User;
import com.finalproject.recruit.exception.authorization.LoginException;
import com.finalproject.recruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    public UserSignupRes signup(String username, String password){
        // 아이디 중복확인
        Optional<User> user = userRepository.findByUserName(username);

        // 회원등록 = DB에 회원정보 저장
        userRepository.save(new User());


        return new UserSignupRes();
    }

    // TODO : DTO 변환필요
    public UserLoginRes login(String username, String password){
        // 회원가입 여부확인
        User userLoginReq = userRepository.findByUserName(username).orElseThrow(() -> new LoginException());

        // 비밀번호 확인
        if(!userLoginReq.getPassword().equals(password)){
            throw new LoginException();
        }

        // 토큰생성

        // 토큰반환
        return null;
    }

}
