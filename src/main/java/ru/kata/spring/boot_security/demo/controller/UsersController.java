package ru.kata.spring.boot_security.demo.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collections;


@Controller
@RequestMapping("/user")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/profile")
    public String showUserProfile(@RequestParam("id") Long id,
                           Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/user/profile";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("updatableUser", userService.getUserById(id));
        return "/user/edit";
    }
    @PatchMapping("/update")
    public String update(@ModelAttribute("updatableUser") @Valid User user,
                         BindingResult bindingResult, @RequestParam("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "/user/edit";
        }
//        user.setRoles(Collections.singleton(new Role("ROLE_USER")));
        userService.updateUserById(id, user);
        return String.format("redirect:/user/profile?id=%s", id);
    }


}
