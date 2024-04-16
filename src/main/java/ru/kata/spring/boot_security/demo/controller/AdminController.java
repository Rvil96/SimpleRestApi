package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;



    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/profile")
    public String showUserProfile(@RequestParam("id") Long id,
                                  Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/profile";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("updatableUser", userService.getUserById(id));
        model.addAttribute("role_list", roleService.getAllRole());
        return "/admin/edit";
    }

    @PatchMapping("/update")
    public String update(@ModelAttribute("updatableUser") @Valid User user,
                         BindingResult bindingResult, @RequestParam("id") Long id) {

        if (bindingResult.hasErrors()) {
            return "/admin/edit";
        }
        userService.updateUserById(id, user);
        return "redirect:/admin/";
    }

    @GetMapping("/")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("allUser", users);
        return "admin/users";
    }

    @GetMapping("/create")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("role_list", roleService.getAllRole());
        return "admin/createUser";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         Model model) {
        if (userService.usernameExist(user.getName())) {
            bindingResult.rejectValue("name", "error.name", "Пользователь с таким именем уже существует");
            return "admin/createUser";
        }

        if (bindingResult.hasErrors()) {
            return "admin/createUser";
        }

        userService.addUser(user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam Long id) {
        userService.removeUserById(id);
        return "redirect:/admin/";
    }
}
