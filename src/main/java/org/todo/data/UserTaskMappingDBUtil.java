package org.todo.data;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.todo.data.entity.Task;
import org.todo.data.entity.User;
import org.todo.data.entity.UserTaskMapId;
import org.todo.data.entity.UserTaskMapping;
import org.todo.data.orm.HibernateController;

public class UserTaskMappingDBUtil {
    private static final Logger LOGGER = LogManager.getLogger(UserTaskMappingDBUtil.class.getName());
    public static int createTaskUserJoin(Task task, User user){
        String creationMappingQuery = "INSERT INTO USER_TASK_MAPPING (USER_ID,TASK_ID) VALUES (?,?);";
        try(Session session = HibernateController.getSession().openSession()){
            UserTaskMapping utm = new UserTaskMapping();
            utm.setId(new UserTaskMapId(task.getTask_id(),user.getId()));
            utm.setUser(user);
            utm.setTask(task);
            Transaction transaction = session.beginTransaction();
            session.persist(utm);
            transaction.commit();
//            Connection connection = DatabaseConnector.getInstance().getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(creationMappingQuery);
//            preparedStatement.setString(1,String.valueOf(task.getUser_id()));
//            preparedStatement.setString(2,String.valueOf(task.getTask_id()));
//            return preparedStatement.executeUpdate();
            if(transaction.isComplete() && transaction.getStatus() == TransactionStatus.COMMITTED){
                return 1;
            }
        }
        catch(Exception exception){
            LOGGER.log(Level.ERROR,exception);
        }
        return 0;
    }
}
