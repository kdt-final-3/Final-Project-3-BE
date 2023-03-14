package com.finalproject.recruit.fixture;

import com.finalproject.recruit.entity.User;

// 테스트 중 비밀번호 비교 로직을 위해 테스트용 User Entity 구성
public class AuthUserFixture {

    public static User get(String username, String password){
        User res = new User();
        res.setId(1);
        res.setUserName(username);
        res.setPassword(password);

        return res;
    }

}
