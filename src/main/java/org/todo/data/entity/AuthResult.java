package org.todo.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AuthResult {
    private User user;
    private int result;
    public AuthResult(int result){
        this.result = result;
    }
}
