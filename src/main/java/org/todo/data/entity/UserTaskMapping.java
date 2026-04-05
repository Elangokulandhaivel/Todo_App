package org.todo.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@Entity @Table(name = "USER_TASK_MAPPING")
public class UserTaskMapping {
    @EmbeddedId
    private UserTaskMapId id;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne
    @MapsId("task_id")
    @JoinColumn(name = "TASK_ID")
    private Task task;
}
