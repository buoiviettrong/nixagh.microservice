package com.nixagh.userservice.service;

import com.nixagh.userservice.dto.LoginDTO;
import com.nixagh.userservice.entiry.User;
import com.nixagh.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final RestTemplate restTemplate;

    public UserService(UserRepository repository,
                       RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }


    public User save(User user) {
        if(this.repository.findByEmail(user.getEmail()).isPresent())
            return null;
        else
            return this.repository.save(user);
    }

    public User getById(String id) {
        return this.repository.findById(id).orElse(null);
    }

    public User login(LoginDTO loginDTO) {
        return this.repository.findByEmail(loginDTO.getEmail()).orElse(null);
    }
}
