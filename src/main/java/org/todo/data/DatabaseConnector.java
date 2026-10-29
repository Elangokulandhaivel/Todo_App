package org.todo.data;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;


import java.lang.ref.Cleaner;
import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.logging.log4j.Logger;
@Deprecated
public class DatabaseConnector {
    private final Logger LOGGER = LogManager.getLogger(DatabaseConnector.class.getName());
    private DatabaseConnector(){

    }
    private static DatabaseConnector instance;
    public static DatabaseConnector getInstance(){
        return instance==null? instance = new DatabaseConnector():instance;
    }
    private Connection connection = null;
    public Connection getConnection(){
        try{
            if(this.connection == null || this.connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo", "root", "Elango2002#");
                cleanable = cleaner.register(this, new CleanupTask(this.connection));
            }
            return this.connection;
        }
        catch(Exception exception){
            LOGGER.log(Level.ERROR,exception);
        }
        return null;
    }
    public void closeConnection(){
        cleanable.clean();
    }

    private static Cleaner cleaner = Cleaner.create();
    private static class CleanupTask implements Runnable {
        Logger CleanerLogger = LogManager.getLogger(CleanupTask.class.getName());
        Connection connection = null;
        CleanupTask(Connection connection){
            this.connection = connection;
        }
        @Override
        public void run() {
            if(closeConnection()) {
                CleanerLogger.log(Level.INFO,"Cleanup when object is garbage collected");
            }
            else{
                CleanerLogger.log(Level.INFO,"Cleanup Failed when object is garbage collected");
            }
        }
        private Boolean closeConnection(){
            try{
                if(connection != null) {
                    this.connection.close();
                }
                return true;
            }
            catch (Exception exception){
                CleanerLogger.log(Level.ERROR,exception);
            }
            return false;
        }
    }
    private Cleaner.Cleanable cleanable;
}
