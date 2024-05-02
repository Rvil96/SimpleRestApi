package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;

import java.security.Principal;

@RestController
@RequestMapping("/userRest")
public class UserRest {
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserRest(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
