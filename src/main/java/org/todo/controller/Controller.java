package org.todo.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.todo.data.entity.Task;
import org.todo.data.entity.User;
import org.todo.service.TaskDataController;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private TaskDataController taskDataController;
    @Autowired
    private ObjectProvider<User> userProvider;
    @GetMapping("/tasks")
    public List<Task> getTaskForUserId(@RequestParam(name = "userId") int userId){
//        int id = Integer.parseInt(userId);
        if(userId==0){
            return null;
        }
        User user = userProvider.getObject();
        user.setId(userId);
        return taskDataController.listTaskWithUserId(user);
    }
}
