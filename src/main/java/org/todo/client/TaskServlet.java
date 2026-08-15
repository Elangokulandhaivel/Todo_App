package org.todo.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.todo.controller.TaskDataController;
import org.todo.data.entity.User;

import java.io.IOException;
import java.io.PrintWriter;

public class TaskServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(TaskServlet.class.getName());

    private ApplicationContext context;

    @Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        LOGGER.log(Level.ERROR,"Context is null - {0}",context==null);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("userId");
        if(userId==null || userId.isEmpty()){
            setReponse(response,"UserId is not present");
        }
        User user = context.getBean("User", User.class);
        user.setId(Integer.parseInt(userId));
        TaskDataController taskDataController = context.getBean("TaskDataController",TaskDataController.class);
        JSONObject tasks = taskDataController.listTaskWithUserId(user);
        setReponse(response,tasks.toString());
    }
    private void setReponse(HttpServletResponse response,String reponseContent) throws IOException {
        try {
            PrintWriter writer = response.getWriter();
            writer.println(reponseContent);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR,"Exception in task servlet",e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Exception in task servlet");
        }
    }
}
