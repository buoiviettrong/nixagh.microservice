package com.nixagh.userservice.controller;

import com.nixagh.userservice.dto.LoginDTO;
import com.nixagh.userservice.entiry.User;
import com.nixagh.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @GetMapping(value = "/secure")
    public String getSecure() {
        return "Secure endpoint available";
    }
}
