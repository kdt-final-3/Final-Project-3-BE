package com.finalproject.recruit.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginReq {
    private String userName;
    private String password;
}
