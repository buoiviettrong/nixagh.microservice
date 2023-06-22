package com.nixagh.userservice.dto;

import com.nixagh.userservice.entiry.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestToken {
    private String user_id;
    private String accessToken;
    private String refreshToken;
}
