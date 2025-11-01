package com.todoapp.todo_api.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Document
@Data
public class TodoItem {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String task;
    private boolean completed;
    private LocalDateTime createdAt;
    private String  username;
}
