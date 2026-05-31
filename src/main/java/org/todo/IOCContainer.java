package org.todo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IOCContainer {
    private static ApplicationContext context;
    protected static void initializeAppContext(){
        IOCContainer.context = new ClassPathXmlApplicationContext("beans.xml");
    }
    public static ApplicationContext getAppContext(){
        return IOCContainer.context;
    }
}
