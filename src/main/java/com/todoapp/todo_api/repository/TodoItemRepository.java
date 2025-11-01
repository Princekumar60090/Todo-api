package com.todoapp.todo_api.repository;

import com.todoapp.todo_api.entity.TodoItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TodoItemRepository extends MongoRepository<TodoItem, ObjectId> {
    List<TodoItem> findByUsername(String username);
}
