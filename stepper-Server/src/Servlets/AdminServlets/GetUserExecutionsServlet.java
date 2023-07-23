package Servlets.AdminServlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.Mapper;
import modules.DataManeger.DataManager;
import modules.flow.execution.FlowExecution;
import services.stepper.FlowExecutionDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "get user executions Servlet",urlPatterns = "/getUserExecutions")

public class GetUserExecutionsServlet extends HttpServlet {

    private Gson gson=new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataManager dataManager=(DataManager) getServletContext().getAttribute("dataManager");
        String username = req.getHeader("username");

        List<FlowExecutionDTO> executions = new ArrayList<>();
        for(FlowExecution execution : dataManager.getStepperData().getFlowExecutions()) {
            if (execution.getOwner().equals(username)) {
                executions.add(Mapper.convertToFlowExecutionDTO(execution));
            }
        }
        String executionsJson = gson.toJson(executions);
        resp.setStatus(HttpServletResponse.SC_OK);
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(executionsJson);
        } catch (Exception e) {
            System.out.println("failed to get user executions... ");
            throw new RuntimeException(e);
        }

    }
}
