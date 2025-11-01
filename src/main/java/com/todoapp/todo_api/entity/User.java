package com.todoapp.todo_api.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
public class User {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Indexed(unique=true)
    private String username;

    private String password;

    List<String> roles;

}
