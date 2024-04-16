package ru.kata.spring.boot_security.demo.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;


@Controller
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("allUser", users);
        return "users";
    }

    @GetMapping("/profile")
    public String showUserProfile(@RequestParam("id") Long id,
                           Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/create")
    public String newUser(@ModelAttribute("user") User user) {
        return "createUser";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createUser";
        }
        userService.addUser(user);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("updatableUser", userService.getUserById(id));
        return "/edit";
    }
    @PatchMapping("/update")
    public String update(@ModelAttribute("updatableUser") @Valid User user,
                         BindingResult bindingResult, @RequestParam("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "/edit";
        }
        userService.updateUserById(id, user);
        return "redirect:/";
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam Long id) {
        userService.removeUserById(id);
        return "redirect:/";
    }

}
