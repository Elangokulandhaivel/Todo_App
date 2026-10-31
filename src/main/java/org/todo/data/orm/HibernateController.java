package org.todo.data.orm;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.todo.data.entity.Task;
import org.todo.data.entity.User;
import org.todo.data.entity.UserTaskMapping;
import org.todo.data.entity.Role;
import org.todo.data.entity.UserRoleMapping;

public class HibernateController {
    private static SessionFactory sessionFactory;
    static{
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Task.class);
        configuration.addAnnotatedClass(UserTaskMapping.class);
        configuration.addAnnotatedClass(UserRoleMapping.class);
        configuration.addAnnotatedClass(Role.class);
        sessionFactory = configuration.buildSessionFactory();
    }
    public static SessionFactory getSession(){
        return sessionFactory;
    }
}
