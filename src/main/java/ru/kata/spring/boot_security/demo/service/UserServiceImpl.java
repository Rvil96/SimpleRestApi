package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        userRepository.save(user);
    }


    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateUserById(long id, User updatedUser) {
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void removeUserById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean usernameExist(String name) {
         return userRepository.existsByName(name);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findUserByName(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found");

        return user;
    }
}
