package org.todo.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.todo.IOCContainer;
import org.todo.data.UserDBUtil;
import org.todo.data.entity.AuthResult;
import org.todo.data.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDataController {
    private final Logger LOGGER = LogManager.getLogger(UserDataController.class.getName());
    @Autowired
    UserDBUtil userDBUtil;
    @Autowired
    IOCContainer iocContainer;
    public int createUser(User user){
        List<User> users = userDBUtil.getUsers().orElse(new ArrayList<>());
        if(users.stream().anyMatch(element -> element.getName().equals(user.getName()))){
            return 1;
        }
        int result = userDBUtil.createUser(user);
        return result==1?0:2;
    }
    public Optional<AuthResult> performAuth(User user){
        Optional<User> userFromDB = userDBUtil.getUser(user.getName());
        AuthResult authResult = iocContainer.getAppContext().getBean("AuthResult", AuthResult.class);
        if(userFromDB.isEmpty()) {
            LOGGER.log(Level.WARN,"There was problem in getting user details...");
            authResult.setResult(2);
            return Optional.of(authResult);
        }
        User dbUser = userFromDB.get();
        authResult.setUser(dbUser);
        if(dbUser.getName().isEmpty()){
            LOGGER.log(Level.INFO,"There is no user present on given name...Kindly create user with this name.");
            authResult.setResult(3);
            return Optional.of(authResult);
        }
        authResult.setResult(user.getPassword().equals(dbUser.getPassword())?0:1);
        return Optional.of(authResult);
    }
}
