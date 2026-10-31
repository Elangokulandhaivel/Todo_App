package org.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.todo.data.entity.AuthResult;
import org.todo.data.entity.Task;
import org.todo.data.entity.User;
import org.todo.service.TaskService;
import org.todo.service.UserService;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
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
        return taskService.listTaskWithUserId(user);
    }
    @PostMapping("/auth")
    public AuthResult authentication(@RequestBody User user, HttpServletRequest request){
        HttpSession session = request.getSession();
        return userService.performAuth(user).orElseThrow();
    }

    @PostMapping("/createTask")
    @ResponseBody
    public int createTask(@RequestBody Task task,@RequestParam(name = "userId") int userId){
        task.setUser_id(userId);
        User user =  userService.getUserWithId(task.getUser_id()).orElseGet(()->{
            User newUser = userProvider.getObject();
            newUser.setId(userId);
            return newUser;
        });
//        Date date = new SimpleDateFormat("DD/MM/YYYY").parse(task.getCompletionDate());
//        createdTask.setDueDate(new Timestamp(date.getTime()));
        task.setCreationDate(new Timestamp(System.currentTimeMillis()));
        return taskService.createTask(task,user);
    }
    @PostMapping("/markTaskAsCompleted")
    @ResponseBody
    public int markTaskAsCompleted(@RequestParam(name = "taskId") int taskId){
        return taskService.markTaskAsComplete(taskService.getTaskUsingTaskId(taskId));
    }
}
