package com.todoapp.todo_api.service;

import com.todoapp.todo_api.entity.User;
import com.todoapp.todo_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFromDb = userRepository.findByUsername(username);

        if (userFromDb != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userFromDb.getUsername())
                    .password(userFromDb.getPassword())
                    .roles("USER")   // IMPORTANT FIX
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
