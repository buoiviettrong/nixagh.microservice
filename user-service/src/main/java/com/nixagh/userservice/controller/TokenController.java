package com.nixagh.userservice.controller;

import com.nixagh.userservice.dto.RequestToken;
import com.nixagh.userservice.entiry.User;
import com.nixagh.userservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tokens")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/save")
    public String save(@RequestBody RequestToken requestToken) {
        return tokenService.save(requestToken);
    }

    @PostMapping("/isWhiteList")
    public String isWhiteList(@RequestBody requestWithSingleToken token) {
        return tokenService.isWhiteList(token.token);
    }

    @PostMapping("/block")
    public String blockToken(@RequestBody requestWithSingleToken token) {
        return tokenService.block(token.token);
    }

    public record requestWithSingleToken(String token) {}
}

