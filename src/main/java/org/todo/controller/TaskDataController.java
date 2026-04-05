package org.todo.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todo.data.entity.Task;
import org.todo.data.TaskDBUtil;
import org.todo.data.entity.User;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

public class TaskDataController {
    private static final Logger LOGGER = LogManager.getLogger(TaskDataController.class.getName());
    public static void createTask(Task task, User user){
        if(TaskDBUtil.createTask(task, user)==1){
            LOGGER.log(Level.INFO,"Task created successfully");
        }
        else{
            LOGGER.log(Level.INFO,"Task creation failed");
        }
    }
    public static void markTaskAsComplete(Task task){
        if(TaskDBUtil.markTaskAsCompleted(task)==1){
            LOGGER.log(Level.INFO,"Task marked as complete.");
        }
        else{
            LOGGER.log(Level.INFO,"Task was not marked as complete");
        }
    }
    public static void listTaskWithUserId(User user){
        List<Task> tasks = TaskDBUtil.getTaskWithUser(user);
        tasks.stream().forEach(task -> {
            LOGGER.log(Level.INFO,MessageFormat.format("Task Name : {0}\nDescription : {1}\nCreation Date : {2}\nDue Date : {3}\nCompletion Date : {4}\nTask id: {5}", new Object[]{task.getName(),task.getDescription(),task.getCreationDate().toLocalDateTime(),task.getDueDate().toLocalDateTime(),(task.getCompletionDate()==null)?"Not Completed":"Completed on "+task.getCompletionDate().toLocalDateTime(),task.getTask_id()}));
        });
    }
    public static Task getTaskUsingTaskId(int taskId){
        Optional<Task> optionalTask = TaskDBUtil.getTaskUsingTaskId(taskId);
        return optionalTask.orElse(null);
//        if(optionalTask.isPresent()){
//            System.out.println(MessageFormat.format("Task Name : {0}\nDescription : {1}\nCreation Date : {2}\nCompletion Date : {3}\nTask id: {4}", new Object[]{task.name,task.description,task.creationDate,task.completionDate,task.task_id}));
//        }
    }
}
