package com.nixagh.userservice.service;

import com.nixagh.userservice.dto.RequestToken;
import com.nixagh.userservice.entiry.Token;
import com.nixagh.userservice.entiry.User;
import com.nixagh.userservice.repository.TokenRepository;
import com.nixagh.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public String save(RequestToken requestToken) {
        User user = userRepository.findById(requestToken.getUser_id()).orElse(null);
        Token token = Token.builder()
                .accessToken(requestToken.getAccessToken())
                .revoked(false)
                .expired(false)
                .user(user)
                .build();
        return tokenRepository.save(token).getToken_id();
    }

    public String isWhiteList(String token) {
        Token databaseToken = tokenRepository.findByAccessToken(token).orElse(null);
        return databaseToken == null || databaseToken.isExpired() || databaseToken.isRevoked() ? null : databaseToken.getToken_id();
    }

    public String block(String token) {
        Token databaseToken = tokenRepository.findByAccessToken(token).orElse(null);

        if (databaseToken == null) return null;

        databaseToken.setRevoked(true);
        databaseToken.setExpired(true);
        tokenRepository.save(databaseToken);
        return databaseToken.getToken_id();
    }
}
