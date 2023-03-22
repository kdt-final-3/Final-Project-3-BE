package com.finalproject.recruit.service;

import com.finalproject.recruit.entity.User;
import com.finalproject.recruit.exception.authorization.LoginException;
import com.finalproject.recruit.exception.authorization.SignupException;
import com.finalproject.recruit.fixture.AuthUserFixture;
import com.finalproject.recruit.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void 회원가입시_정상적으로_동작(){
        String username = "";
        String password = "";

        // 테스트용 User Entity
        User fixture = AuthUserFixture.get(username, password);

        // mocking
        // 실제 가입된 상황이 아니기에, find를 하여도 결과가 없도록 empty() return
        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());
        // save() 메소드는 저장된 값을 return 하기에 mock(User.class) return
        when(userRepository.save(any())).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(()-> authService.signup(username, password));
    }

    @Test
    void 회원가입시_이미_등록된_유저아이디로_회원가입을_시도(){
        String username = "";
        String password = "";

        // 테스트용 User Entity
        User fixture = AuthUserFixture.get(username, password);

        // mocking
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fixture));
        when(userRepository.save(any())).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(SignupException.class, ()-> authService.signup(username, password));
    }

    @Test
    void 로그인시_정상적으로_동작(){
        String username = "";
        String password = "";

        // 테스트용 User Entity
        User fixture = AuthUserFixture.get(username, password);

        // mocking
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(()-> authService.login(username, password));
    }

    @Test
    void 로그인시_가입된_정보가_없는경우(){
        String username = "";
        String password = "";

        // mocking
        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(LoginException.class, ()-> authService.signup(username, password));
    }

    @Test
    void 로그인시_비밀번호가_틀린경우(){
        String username = "";
        String password = "";
        String wrongPassword = "";

        // 테스트용 User Entity
        User fixture = AuthUserFixture.get(username, password);

        // mocking
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(LoginException.class, ()-> authService.signup(username, wrongPassword));
    }
}
