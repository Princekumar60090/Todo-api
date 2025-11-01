package com.todoapp.todo_api.controller;

import com.todoapp.todo_api.entity.User;
import com.todoapp.todo_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")

public class PublicController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        try {
            boolean isSaved = userService.saveNewUser(user);
            if (isSaved) {
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            } else {
                // Send a 409 Conflict status if the user already exists
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}