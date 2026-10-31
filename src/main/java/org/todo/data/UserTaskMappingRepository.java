package org.todo.data;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.todo.IOCContainer;
import org.todo.data.entity.Task;
import org.todo.data.entity.User;
import org.todo.data.entity.UserTaskMapId;
import org.todo.data.entity.UserTaskMapping;
import org.todo.data.orm.HibernateController;
@Repository
public class UserTaskMappingRepository {
    private final Logger LOGGER = LogManager.getLogger(UserTaskMappingRepository.class.getName());
    @Autowired
    IOCContainer iocContainer;
    public int createTaskUserJoin(Task task, User user){
        String creationMappingQuery = "INSERT INTO USER_TASK_MAPPING (USER_ID,TASK_ID) VALUES (?,?);";
        try(Session session = HibernateController.getSession().openSession()){
            UserTaskMapping utm = iocContainer.getAppContext().getBean("UserTaskMapping", UserTaskMapping.class);
            UserTaskMapId userTaskMapId = iocContainer.getAppContext().getBean("UserTaskMapId",UserTaskMapId.class);
            userTaskMapId.setTask_id(task.getTask_id());
            userTaskMapId.setUser_id(user.getId());
            utm.setId(userTaskMapId);
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
