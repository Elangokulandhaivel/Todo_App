package org.todo.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_role_mapping")
public class UserRoleMapping {
    @EmbeddedId
    private UserRoleMappingId id;

    @JoinColumn(name="user_id")
    @ManyToOne
    @MapsId("user_id")
    User user;
    @JoinColumn(name="role_id")
    @ManyToOne
    @MapsId("role_id")
    Role role;
}
