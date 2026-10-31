package org.todo.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name="role_id")
    private int role_id;
    @Column(name="role")
    private String role_name;
    @OneToMany(mappedBy = "role")
    private List<UserRoleMapping> users;
}
