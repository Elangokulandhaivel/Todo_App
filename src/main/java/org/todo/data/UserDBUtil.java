package org.todo.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.todo.data.entity.User;
import org.todo.data.orm.HibernateController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDBUtil {
    private static final Logger LOGGER = LogManager.getLogger(UserDBUtil.class.getName());
    public static int createUser(User user){
        try(Session session = HibernateController.getSession().openSession()){
            session.persist("User",user);
            Transaction transaction = session.beginTransaction();
            transaction.commit();
            TransactionStatus status = transaction.getStatus();
            if(status == TransactionStatus.COMMITTED){
                return 1;
            }
        }
        catch (NoResultException | NonUniqueResultException noResultException){
            LOGGER.log(Level.ERROR,noResultException);
            return 0;
        }
//        String createUserQuery = "INSERT INTO USERS(NAME,PASSWORD) VALUES(?,?);";
//        try {
//            Connection connection = DatabaseConnector.getInstance().getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(createUserQuery);
//            preparedStatement.setString(1,user.getName());
//            preparedStatement.setString(2,user.getPassword());
//            int affectedRows = preparedStatement.executeUpdate();
//            if(affectedRows==1){
//                LOGGER.log(Level.INFO,"User Inserted Successfully...");
//                return affectedRows;
//            }
//            LOGGER.log(Level.INFO,"User Inserted Failed...");
//        }
//        catch(Exception exception){
//            exception.printStackTrace();
//        }
        return 1;
    }
    public static Optional<List<User>> getUsers(){
        try(Session session = HibernateController.getSession().openSession()){
            SelectionQuery<User> query = session.createSelectionQuery("FROM User",User.class);
            return Optional.of(query.getResultList());
        }
        catch (NoResultException | NonUniqueResultException noResultException){
            LOGGER.log(Level.ERROR,noResultException);
        }
//        String selectUserQuery = "SELECT * FROM USERS;";
//        try{
//            Connection connection = DatabaseConnector.getInstance().getConnection();
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(selectUserQuery);
//            List<User> users = new ArrayList<>();
//            while(resultSet.next()){
//                User user = new User();
//                user.setName(resultSet.getString("NAME"));
//                users.add(user);
//            }
//            return Optional.of(users);
//        }
//        catch (Exception exception){
//            LOGGER.log(Level.ERROR,exception);
//        }
        return Optional.empty();
    }
    public static Optional<User> getUser(String userName){
        try(Session session = HibernateController.getSession().openSession()){
            Query<User> query = session.createQuery("FROM User us WHERE us.name = ?1",User.class);
            query.setParameter(1,userName);
            User user = query.getSingleResult();
            return Optional.of(user);
        }
        catch (NoResultException | NonUniqueResultException noResultException){
            LOGGER.log(Level.ERROR,noResultException);
        }
//        String selectUserQuery = "SELECT ID,PASSWORD FROM USERS WHERE NAME=?;";
//        try{
//            Connection connection = DatabaseConnector.getInstance().getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery);
//            preparedStatement.setString(1,userName);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            User user = new User();
//            user.setName(userName);
//            while(resultSet.next()){
//                user.setId(resultSet.getInt("ID"));
//                user.setPassword(resultSet.getString("PASSWORD"));
//            }
//            user.setPassword(user.getPassword()==null?"":user.getPassword());
//            return Optional.of(user);
//        }
//        catch(Exception exception){
//            LOGGER.log(Level.ERROR,exception);
//        }
        return Optional.empty();
    }
}
