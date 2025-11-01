package com.todoapp.todo_api.service;

import com.todoapp.todo_api.entity.User;
import com.todoapp.todo_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean saveNewUser(User user){

        if(userRepository.findByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true; // User was successfully saved
        }
        return false; // User already exists, not saved
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}