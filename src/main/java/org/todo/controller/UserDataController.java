package org.todo.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todo.data.UserDBUtil;
import org.todo.data.entity.AuthResult;
import org.todo.data.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDataController {
    private static final Logger LOGGER = LogManager.getLogger(UserDataController.class.getName());
    public static int createUser(User user){
        List<User> users = UserDBUtil.getUsers().orElse(new ArrayList<>());
        if(users.stream().anyMatch(element -> element.getName().equals(user.getName()))){
            return 1;
        }
        int result = UserDBUtil.createUser(user);
        return result==1?0:2;
    }
    public static Optional<AuthResult> performAuth(User user){
        Optional<User> userFromDB = UserDBUtil.getUser(user.getName());
        AuthResult authResult = new AuthResult();
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
