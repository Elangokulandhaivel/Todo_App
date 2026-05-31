package org.todo;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todo.controller.TaskDataController;
import org.todo.controller.UserDataController;
import org.todo.data.DatabaseConnector;
import org.todo.data.entity.AuthResult;
import org.todo.data.entity.Task;
import org.todo.data.entity.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

public class Main {
private static Logger logger = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) {
        logger.log(Level.INFO, "Starting the Application....");
        IOCContainer.initializeAppContext();
        boolean run = true;
        Scanner scanner = new Scanner(System.in);
        while(run){
            System.out.println("\nEnter option: \n1: create User\n2: Auth user\n3.exit");
            int option = 4;
            try{
                 option = Integer.parseInt(scanner.nextLine());
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
            switch(option){
                case 1:
                    UserDataController.createUser(createUser(scanner));
                    break;
                case 2:
                    User user = createUser(scanner);
                    Optional<AuthResult> authResult = UserDataController.performAuth(user);
                    authUser(authResult.orElse(IOCContainer.getAppContext().getBean("AuthResult",AuthResult.class)));
                    break;
                case 3:
                    DatabaseConnector.getInstance().closeConnection();
                    run = false;
                    System.out.println("Exited...");
                    break;
                default:
                    System.out.println("\nEnter correct Option...");
                    break;
            }
        }
        boolean secondFlag = true;
        while(secondFlag){
            System.out.println("\nEnter one to exit\n");
            int choic = Integer.parseInt(scanner.nextLine());
            switch(choic){
                case 1 :{
                    secondFlag = false;
                }
            }
        }

    }
    private static User createUser(Scanner scanner){
        User user = IOCContainer.getAppContext().getBean("User",User.class);
        System.out.println("\nEnter Name:");
        user.setName(scanner.nextLine());
        System.out.println("\nEnter Password:");
        user.setPassword(scanner.nextLine());
        return user;
    }

    private static void authUser(AuthResult authResult){
        switch(authResult.getResult()){
            case 0:
                System.out.println("Auth Success");
                taskView(authResult.getUser());
                break;
            case 1:
                System.out.println("Auth failed");
                //handle auth failure
                return;
            case 2:
                System.out.println("Internal Error");
                //Internal error
                break;
            case 3:
                System.out.println("Unknown User");
                //No user present in name
                break;
        }
    }

    private static void taskView(User user){
        Scanner sc = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.println("Enter the Choice:\n1.List Task\n2.Mark as completed\n3.Create Task\n4.Exit\n");
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    TaskDataController.listTaskWithUserId(user);
                    break;
                case 2:
                    TaskDataController.listTaskWithUserId(user);
                    System.out.println("Enter the task id to mark as complete.\n");
                    int task_id = Integer.parseInt(sc.nextLine());
                    Task task = TaskDataController.getTaskUsingTaskId(task_id);
                    TaskDataController.markTaskAsComplete(task);
                    break;
                case 3:
                    System.out.println("Enter Name of Task:");
                    String taskName = sc.nextLine();
                    System.out.println("Enter Description for task:");
                    String description = sc.nextLine();
                    System.out.println("Enter targeted date of completion in DD/MM/YYYY:");
                    Date date = null;
                    try{
                        date = new SimpleDateFormat("DD/MM/YYYY").parse(sc.nextLine());
                    }catch(Exception exception){
                        exception.printStackTrace();
                    }
                    Task createdTask = IOCContainer.getAppContext().getBean("Task",Task.class);
                    createdTask.setName(taskName);
                    createdTask.setDescription(description);
                    createdTask.setDueDate(new Timestamp(date.getTime()));
                    createdTask.setCreationDate(new Timestamp(System.currentTimeMillis()));
                    createdTask.setUser_id(user.getId());
                    TaskDataController.createTask(createdTask, user);
                    break;
                case 4:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid choice Please Re-enter\n");
            }
        }
    }

//    public static void main(String args[]){
//        boolean run = true;
//        Scanner sc = new Scanner(System.in);
//        User loggindInUser = null;
//        while(run){
//            System.out.println("Enter the action: \n1. Create User\n2. Login\n3. List Task\n4. Create Task\n");
//            int option = Integer.parseInt(sc.nextLine());
//            switch(option){
//                case 1 : {
//                    System.out.println("Enter the name");
//                    String name = sc.nextLine();
//                    System.out.println("Enter the password");
//                    String password = sc.nextLine();
//                    User user = new User();
//                    user.id = 1;
//                    user.name = name;
//                    user.password = password;
//                    Data.users.add(user);
//                    break;
//                }
//                case 2 :{
//                    System.out.println("Enter the userId");
//                    int id = Integer.parseInt(sc.nextLine());
//                    System.out.println("Enter the password");
//                    String password = sc.nextLine();
//                    for(User user : Data.users){
//                        if(user.id == id && password.equals(user.password)){
//                            loggindInUser = user;
//                        }
//                    }
//                    break;
//                }
//                case 3 :{
//                        for(Task task : loggindInUser.tasks){
//                            System.out.println("Task name: "+task.name);
//                            System.out.println("Task description: "+task.description);
//                        }
//                        break;
//                }
//                case 4 : {
//                    System.out.println("Enter task name");
//                    String name = sc.nextLine();
//
//                    System.out.println("Enter task description");
//                    String description = sc.nextLine();
//                    loggindInUser.tasks.add(new Task(1,name,description));
//                    break;
//                }
//                case 5 :{
//                    System.out.println("Ending....");
//                    run = false;
//                    break;
//                }
//                default:{
//                    System.out.println("Re-Enter the option...");
//                }
//            }
//        }
//    }
}


