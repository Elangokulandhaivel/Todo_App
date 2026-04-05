package org.todo.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import java.sql.Timestamp;
@Data @Entity @NoArgsConstructor
public class Task{
    private int user_id;
    @Id
    @Column(name = "TASK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int task_id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;
    @Column(name = "DUE_DATE")
    private Timestamp dueDate;
    @Column(name = "COMPLETION_DATE")
    private Timestamp completionDate;
    @OneToMany(mappedBy = "task")
    private List<UserTaskMapping> users;

    public Task(String name, String description){
        this.name = name;
        this.description = description;
    }
}