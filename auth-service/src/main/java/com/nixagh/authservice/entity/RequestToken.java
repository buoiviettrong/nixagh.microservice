package com.nixagh.authservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestToken {
    private String user_id;
    private String accessToken;
    private String refreshToken;
}
