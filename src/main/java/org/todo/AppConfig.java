package org.todo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.todo.data.entity.*;

@Configuration
public class AppConfig {
    @Bean(name = "User")
    @Scope("prototype")
    public User user(){
        return new User();
    }
    @Bean(name= "Task")
    @Scope("prototype")
    public Task task(){
        return new Task();
    }
    @Bean(name = "AuthResult")
    @Scope("prototype")
    public AuthResult authResult(){
        return new AuthResult(2);
    }
    @Bean(name = "UserTaskMapping")
    @Scope("prototype")
    public UserTaskMapping userTaskMapping(){
        return new UserTaskMapping();
    }
    @Bean(name = "UserTaskMapId")
    public UserTaskMapId userTaskMapId(){
        return new UserTaskMapId();
    }
    @Bean(name = "IOCContainer")
    public IOCContainer iocContainer(){
        return new IOCContainer();
    }

}
