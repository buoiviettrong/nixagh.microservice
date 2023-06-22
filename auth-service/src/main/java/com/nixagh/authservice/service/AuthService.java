package com.nixagh.authservice.service;

import com.nixagh.authservice.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    private final String userServiceURL = "http://localhost:8762/users";
    private final String tokenServiceURL = "http://localhost:8762/tokens";
    private final RestTemplate restTemplate;
    private final JwtUtil jwt;

    @Value("${jwt.salt}")
    private String salt;

    @Autowired
    public AuthService(RestTemplate restTemplate, final JwtUtil jwt) {
        this.restTemplate = restTemplate;
        this.jwt = jwt;
    }

    public AuthResponse register(RegisterRequest authRequest) {
        List<String> errorMessage = new ArrayList<String>();
        //do validation if user already exists
        authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt(12)));

        UserDTO userDTO = restTemplate.postForObject(userServiceURL, authRequest, UserDTO.class);
        if(userDTO == null) {
            errorMessage.add("Email already exist!");
            return new AuthResponse(errorMessage, null, null);
        }

        return generateToken(userDTO);
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        List<String> errorMessage = new ArrayList<String>();

        UserDTO userDTO = restTemplate.postForObject(userServiceURL + "/login", authRequest, UserDTO.class);

        if(userDTO == null) errorMessage.add("Wrong email!");
        // check password
        else
            if(!BCrypt.checkpw(authRequest.getPassword(), userDTO.getPassword()))
                errorMessage.add("Password not equal!");

        if(errorMessage.size() > 0) return new AuthResponse(errorMessage, null, null);

        return generateToken(userDTO);
    }

    private AuthResponse generateToken(UserDTO userDTO) {
        String accessToken = jwt.generate(userDTO, "ACCESS");
        String refreshToken = jwt.generate(userDTO, "REFRESH");

        RequestToken requestToken = RequestToken.builder()
                .user_id(userDTO.getUser_id())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        String token_id = restTemplate.postForObject(tokenServiceURL + "/save", requestToken, String.class);

        return new AuthResponse(null, accessToken, refreshToken);
    }

    public void logout(String token) {
        String token_id = restTemplate.postForObject(tokenServiceURL + "/block", token, String.class);
    }
}
