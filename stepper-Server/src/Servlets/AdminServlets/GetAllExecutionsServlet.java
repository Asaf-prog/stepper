package Servlets.AdminServlets;

import Servlets.userManager.UserManager;
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

@WebServlet(name = "get all executions",urlPatterns = "/getAllExecutions")

public class GetAllExecutionsServlet extends HttpServlet {
    Gson gson=new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        DataManager dataManager=(DataManager)req.getServletContext().getAttribute("dataManager");
        List<FlowExecutionDTO> res=buildList(dataManager.getStepperData().getFlowExecutions());
        resp.getWriter().println(gson.toJson(res));

    }

    private List<FlowExecutionDTO> buildList(List<FlowExecution> flowExecutions) {
        UserManager userManager=(UserManager) getServletContext().getAttribute("userManager");


        List<FlowExecutionDTO> flowExecutionDTOS=new ArrayList<>();
        for (FlowExecution flowExecution:flowExecutions){
            if (userManager.isUserExists(flowExecution.getOwner())==false)//means that the user is not logged in!
                continue;
            flowExecutionDTOS.add(Mapper.convertToFlowExecutionDTO(flowExecution));
        }
        return flowExecutionDTOS;
    }
}
