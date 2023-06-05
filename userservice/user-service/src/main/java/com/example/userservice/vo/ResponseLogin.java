package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseLogin {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String userId;
}
