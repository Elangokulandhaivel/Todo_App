package org.todo.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UserRoleDetails {
    String userName;
    String password;
    String roleName;
    public UserRoleDetails(String userName, String password, String roleName){
        this.userName = userName;
        this.password = password;
        this.roleName = roleName;
    }
}
