package org.todo.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data @Embeddable @NoArgsConstructor
public class UserTaskMapId implements Serializable {
    @Column(name="USER_ID")
    private int user_id;
    @Column(name = "TASK_ID")
    private int task_id;
    public UserTaskMapId(int taskid, int userid){
        this.task_id = taskid;
        this.user_id = userid;
    }
}
