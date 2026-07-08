package org.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IOCContainer {
    @Autowired
    private ApplicationContext context;
    protected void initializeAppContext(){
        this.context = new ClassPathXmlApplicationContext("../WEB-INF/beans.xml");
    }
    public ApplicationContext getAppContext(){
        return this.context;
    }
}
