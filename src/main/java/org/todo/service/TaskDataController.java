package org.todo.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.todo.data.entity.Task;
import org.todo.data.TaskDBUtil;
import org.todo.data.entity.User;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
@Service
public class TaskDataController {
    private final Logger LOGGER = LogManager.getLogger(TaskDataController.class.getName());
    @Autowired
    TaskDBUtil taskDBUtil;
    public int createTask(Task task, User user){
        if(taskDBUtil.createTask(task, user)==1){
            LOGGER.log(Level.INFO,"Task created successfully");
            return 1;
        }
        else{
            LOGGER.log(Level.INFO,"Task creation failed");
            return 0;
        }
    }
    public int markTaskAsComplete(Task task){
        if(taskDBUtil.markTaskAsCompleted(task)==1){
            LOGGER.log(Level.INFO,"Task marked as complete.");
            return 1;
        }
        else{
            LOGGER.log(Level.INFO,"Task was not marked as complete");
            return 0;
        }
    }
    public JSONObject listTaskWithUserIdReturnJSON(User user){
        List<Task> tasks = taskDBUtil.getTaskWithUser(user);
        JSONArray array = new JSONArray(tasks);
        tasks.stream().forEach(task -> {
            LOGGER.log(Level.INFO,MessageFormat.format("Task Name : {0}\nDescription : {1}\nCreation Date : {2}\nDue Date : {3}\nCompletion Date : {4}\nTask id: {5}", new Object[]{task.getName(),task.getDescription(),task.getCreationDate().toLocalDateTime(),task.getDueDate().toLocalDateTime(),(task.getCompletionDate()==null)?"Not Completed":"Completed on "+task.getCompletionDate().toLocalDateTime(),task.getTask_id()}));
        });
        return new JSONObject().put("tasks",array);
    }
    public List<Task> listTaskWithUserId(User user){
        return taskDBUtil.getTaskWithUser(user);
    }
    public Task getTaskUsingTaskId(int taskId){
        Optional<Task> optionalTask = taskDBUtil.getTaskUsingTaskId(taskId);
        return optionalTask.orElse(null);
//        if(optionalTask.isPresent()){
//            System.out.println(MessageFormat.format("Task Name : {0}\nDescription : {1}\nCreation Date : {2}\nCompletion Date : {3}\nTask id: {4}", new Object[]{task.name,task.description,task.creationDate,task.completionDate,task.task_id}));
//        }
    }
}
