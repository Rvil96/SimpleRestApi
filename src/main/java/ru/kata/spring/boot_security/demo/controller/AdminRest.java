package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserOnlyId;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminRest {
    private  final UserService userService;

    @Autowired
    public AdminRest(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        UserDetailsService detailsService = (UserDetailsService) userService;
        User user = (User) detailsService.loadUserByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody UserOnlyId userOnlyId) {
        userService.removeUserById(userOnlyId.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> saveUser(@RequestBody @Valid User user,
                                               BindingResult bindingResult) {
        if (userService.emailExist(user.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        userService.addUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam long id,
                                                 @RequestBody @Valid User user,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        user.setId(id);
        userService.addUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
