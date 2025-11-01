package com.todoapp.todo_api.controller;
import java.util.*;
import com.todoapp.todo_api.entity.TodoItem;
import com.todoapp.todo_api.service.TodoItemService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")

public class TodoController {

    @Autowired
    private TodoItemService todoItemService;

    @GetMapping
    public ResponseEntity<List<TodoItem>> getAllTodosForUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =authentication.getName();
        List<TodoItem> todos = todoItemService.findByUsername(username);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TodoItem> createTodo(@RequestBody TodoItem todoItem){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        todoItemService.saveNewTodoItem(todoItem,username);
        return new ResponseEntity<>(todoItem,HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodoById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<TodoItem> todo = todoItemService.findById(id);

        if (todo.isPresent() && todo.get().getUsername().equals(username)) {
            return new ResponseEntity<>(todo.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodoItem(@PathVariable ObjectId id, @RequestBody TodoItem newTodoData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<TodoItem> oldTodo = todoItemService.findById(id);

        if (oldTodo.isPresent() && oldTodo.get().getUsername().equals(username)) {
            todoItemService.updateTodoItem(id, newTodoData);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // DELETE a task by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodoById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<TodoItem> todo = todoItemService.findById(id);

        if (todo.isPresent() && todo.get().getUsername().equals(username)) {
            todoItemService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}


