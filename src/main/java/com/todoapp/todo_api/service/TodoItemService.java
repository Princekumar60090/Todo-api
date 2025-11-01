package com.todoapp.todo_api.service;

import com.todoapp.todo_api.entity.TodoItem;
import com.todoapp.todo_api.repository.TodoItemRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoItemService {
    @Autowired
    private TodoItemRepository todoItemRepository;

    public List<TodoItem> findByUsername(String username){
        return todoItemRepository.findByUsername(username);
    }

    public void saveNewTodoItem(TodoItem todoItem,String username){
        todoItem.setCreatedAt(LocalDateTime.now());
        todoItem.setUsername(username);
        todoItemRepository.save(todoItem);
    }

    public void deleteById(ObjectId id){
        todoItemRepository.deleteById(id);
    }


    public Optional<TodoItem> findById(ObjectId id){
        return todoItemRepository.findById(id);
    }


    public void updateTodoItem(ObjectId id,TodoItem newTodoData){
        Optional<TodoItem> oldTodoOptional = todoItemRepository.findById(id);
        if(oldTodoOptional.isPresent()){
            TodoItem oldTodo = oldTodoOptional.get();

            oldTodo.setTask(newTodoData.getTask());
            oldTodo.setCompleted(newTodoData.isCompleted());
            todoItemRepository.save(oldTodo);
        }
    }



}
