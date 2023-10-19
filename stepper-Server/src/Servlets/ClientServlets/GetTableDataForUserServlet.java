package Servlets.ClientServlets;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.Mapper;
import modules.DataManeger.DataManager;
import modules.DataManeger.users.StepperUser;
import modules.flow.execution.FlowExecution;
import services.stepper.FlowExecutionDTO;
import utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetTableDataForUserServlet", urlPatterns = {"/Client/getTableDataForUser"})
public class GetTableDataForUserServlet extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserManager userManager =(UserManager)req.getServletContext().getAttribute("userManager");
        String username = SessionUtils.getUsername(req);
        DataManager dataManager=(DataManager)req.getServletContext().getAttribute("dataManager");
        resp.setContentType("application/json");
        StepperUser user=userManager.getUser(username);
        List<FlowExecutionDTO> res=buildList(dataManager.getStepperData().getFlowExecutions(),user);
        if (res==null){
            res=new ArrayList<>();
        }
        resp.getWriter().println(gson.toJson(res));

    }
    private List<FlowExecutionDTO> buildList(List<FlowExecution> flowExecutions, StepperUser user) {
        if (user.getIsManager()) {
            List<FlowExecutionDTO> flowExecutionDTOS = new ArrayList<>();
            for (FlowExecution flowExecution : flowExecutions) {
                flowExecutionDTOS.add(Mapper.convertToFlowExecutionDTO(flowExecution));
            }
            return flowExecutionDTOS;
        } else {
            List<FlowExecutionDTO> flowExecutionDTOS = new ArrayList<>();
            for (FlowExecution flowExecution : flowExecutions) {
                if (flowExecution.getOwner().equals(user.getUsername())) {
                    flowExecutionDTOS.add(Mapper.convertToFlowExecutionDTO(flowExecution));
                }
            }
            return flowExecutionDTOS;
        }
    }
}
