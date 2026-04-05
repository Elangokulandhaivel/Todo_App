package org.todo.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data @Entity
@Table(name = "users")
public class User{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;
    @OneToMany(mappedBy = "user")
    private List<UserTaskMapping> tasks;
}
