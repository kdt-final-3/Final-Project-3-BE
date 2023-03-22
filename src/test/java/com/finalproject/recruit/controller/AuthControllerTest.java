package com.finalproject.recruit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.recruit.dto.UserLoginRes;
import com.finalproject.recruit.dto.UserSignupReq;
import com.finalproject.recruit.dto.UserSignupRes;
import com.finalproject.recruit.exception.authorization.LoginException;
import com.finalproject.recruit.exception.authorization.SignupException;
import com.finalproject.recruit.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    public void 회원가입() throws Exception{
        String username = "";
        String password = "";

        when(authService.signup(username, password)).thenReturn(mock(UserSignupRes.class));

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserSignupReq(username, password)))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_등록된_아이디로_시도하는_경우_에러반환() throws Exception{
        String username = "";
        String password = "";

        when(authService.signup(username, password)).thenThrow(new SignupException());

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserSignupReq(username, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception{
        String username = "";
        String password = "";

        when(authService.login(username, password)).thenReturn(mock(UserLoginRes.class));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserSignupReq(username, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입이_안된_계정을_입력하는경우_에러반환() throws Exception{
        String username = "";
        String password = "";

        when(authService.login(username, password)).thenThrow(new LoginException());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserSignupReq(username, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린_비밀번호_입력시_에러반환() throws Exception{
        String username = "";
        String password = "";

        when(authService.login(username, password)).thenThrow(new LoginException());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserSignupReq(username, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
