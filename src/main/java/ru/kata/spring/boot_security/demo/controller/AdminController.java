package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
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

    @GetMapping("/")
    public String showAdminPage(Principal principal, Model model) {

        UserDetailsService userDetailsService = (UserDetailsService) userService;
        model.addAttribute("user", userDetailsService.loadUserByUsername(principal.getName()));

        List<User> users = userService.getAllUsers();
        model.addAttribute("allUser", users);
        model.addAttribute("role_list", roleService.getAllRole());

        return "/admin";
    }


    @PatchMapping("/update")
    public String update(@ModelAttribute @Valid User user,
                         BindingResult bindingResult, @RequestParam("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/";
        }

        if (user.getPassword() == null) {
            String userPassword = userService.getUserById(id).getPassword();
            user.setPassword(userPassword);
        }
        userService.updateUserById(id, user);

        return "redirect:/admin/";
    }


    @PostMapping("/addUser")
    public String create(@ModelAttribute @Valid User user, BindingResult bindingResult) {

        if (userService.emailExist(user.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Пользователь с таким именем уже существует");
            return "redirect:/admin/";
        }
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/";
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
