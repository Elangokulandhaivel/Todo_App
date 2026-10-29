package org.todo.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class UserRoleMappingId {
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "role_id")
    private int role_id;
    public UserRoleMappingId(int user_id, int role_id){
        this.role_id = role_id;
        this.user_id = user_id;
    }
}
