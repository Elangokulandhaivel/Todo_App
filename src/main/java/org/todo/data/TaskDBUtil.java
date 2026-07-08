package org.todo.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.criteria.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.todo.data.entity.Task;
import org.todo.data.entity.User;
import org.todo.data.entity.UserTaskMapId;
import org.todo.data.entity.UserTaskMapping;
import org.todo.data.orm.HibernateController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDBUtil {
    private final Logger LOGGER = LogManager.getLogger(TaskDBUtil.class.getName());
    @Autowired
    UserTaskMappingDBUtil userTaskMappingDBUtil;
    public int createTask(Task task, User user){
        String taskCreationQuery = "INSERT INTO TASK (NAME,DESCRIPTION,CREATION_DATE,DUE_DATE) VALUES (?,?,CURRENT_TIMESTAMP,?)";
        try(Session session = HibernateController.getSession().openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
            if(transaction.isComplete() && transaction.getStatus() == TransactionStatus.COMMITTED) {
                return userTaskMappingDBUtil.createTaskUserJoin(task, user);
            }
        }
        catch(Exception exception){
            LOGGER.log(Level.ERROR,exception);
        }
        return 0;
    }
    public int markTaskAsCompleted(Task task){
        if(task != null) {
            String taskCompletionQuery = "UPDATE TABLE TASK SET COMPLETION_DATE=CURRENT_DATE WHERE TASK_ID=?;";
            try(Session session = HibernateController.getSession().openSession()){
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaUpdate<Task> updateQuery = builder.createCriteriaUpdate(Task.class);
                Root<Task> root = updateQuery.from(Task.class);
                updateQuery.set("completionDate",builder.currentTimestamp()).where(builder.equal(root.get("task_id"), task.getTask_id()));
                Transaction transaction = session.beginTransaction();
                int output = session.createQuery(updateQuery).executeUpdate();
                transaction.commit();
//                Connection connection = DatabaseConnector.getInstance().getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement(taskCompletionQuery);
//                preparedStatement.setString(1, String.valueOf(task.getTask_id()));
//                return preparedStatement.executeUpdate();
                return output;
            } catch (Exception exception) {
                LOGGER.log(Level.ERROR,exception);
            }
        }
        return 0;
    }
    public List<Task> getTaskWithUser(User user){
        String createSelectQuery = "SELECT UTM.USER_ID, UTM.TASK_ID, TA.NAME, TA.DESCRIPTION, TA.CREATION_DATE, TA.DUE_DATE, TA.COMPLETION_DATE FROM USER_TASK_MAPPING UTM INNER JOIN TASK TA ON UTM.TASK_ID = TA.TASK_ID WHERE UTM.USER_ID = ?;";
        List<Task> tasks = new ArrayList<>();
        try(Session session = HibernateController.getSession().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            Join<Task, UserTaskMapping> userJoin = root.join("users", JoinType.INNER);
            Join<UserTaskMapping,User> userTaskJoin = userJoin.join("user",JoinType.INNER);
            query.select(root).where(builder.equal(userTaskJoin.get("id"),user.getId()));
            tasks = session.createQuery(query).getResultList();
//            Connection connection = DatabaseConnector.getInstance().getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(createSelectQuery);
//            preparedStatement.setString(1,String.valueOf(user.getId()));
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()){
//                Task task = new Task(resultSet.getString("NAME"),resultSet.getString("DESCRIPTION"));
//                task.setTask_id(resultSet.getInt("TASK_ID"));
//                task.setCreationDate(resultSet.getTimestamp("CREATION_DATE"));
//                task.setDueDate(resultSet.getTimestamp("DUE_DATE"));
//                task.setCompletionDate(resultSet.getTimestamp("COMPLETION_DATE"));
//                tasks.add(task);
//            }
        }
        catch(Exception exception){
            LOGGER.log(Level.ERROR,exception);
        }
        return tasks;
    }
    public Optional<Task> getTaskUsingTaskId(int taskId){
        Task task = null;
        try(Session session = HibernateController.getSession().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root).where(builder.equal(root.get("task_id"),taskId));
            Query<Task> executableQuery = session.createQuery(query);
            task = executableQuery.getSingleResult();
//            SelectionQuery<Task> selectionQuery = session.createSelectionQuery("FROM TASK t WHERE t.TASK_ID = 1?",Task.class);
//            selectionQuery.setParameter(1,taskId);
//            task = selectionQuery.getSingleResult();
            if(task!=null){
                return Optional.of(task);
            }
        }
        catch (NoResultException | NonUniqueResultException noResultException){
            LOGGER.log(Level.ERROR,noResultException);
        }
//        String selectQuery = "SELECT TA.TASK_ID, TA.NAME, TA.DESCRIPTION, TA.CREATION_DATE, TA.DUE_DATE, TA.COMPLETION_DATE FROM TASK WHERE TA.TASK_ID = ?";
//        try{
//            Connection connection = DatabaseConnector.getInstance().getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
//            preparedStatement.setString(1,String.valueOf(taskId));
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()){
//                task = new Task(resultSet.getString("NAME"),resultSet.getString("DESCRIPTION"));
//                task.setTask_id(resultSet.getInt("TASK_ID"));
//                task.setCreationDate(resultSet.getTimestamp("CREATION_DATE"));
//                task.setDueDate(resultSet.getTimestamp("DUE_DATE"));
//                task.setCompletionDate(resultSet.getTimestamp("COMPLETION_DATE"));
//                return Optional.of(task);
//            }
//        }
//        catch(Exception exception){
//            LOGGER.log(Level.ERROR,exception);
//        }
        return Optional.empty();
    }
}
