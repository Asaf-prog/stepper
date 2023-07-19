package Servlets.ClientServlets.execution;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.Mapper;
import modules.DataManeger.DataManager;
import modules.flow.execution.FlowExecution;
import services.stepper.FlowExecutionDTO;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "get last execution Servlet",urlPatterns = "/Client/getLastExecution")
@MultipartConfig
public class GetLastExecutionServlet extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get dataManager from context
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        FlowExecution execution = getLastExecutionForUser(dataManager.getStepperData().getFlowExecutions(), SessionUtils.getUsername(req));
        FlowExecutionDTO flowExecutionDTO = Mapper.convertToFlowExecutionDTO(execution);
        //gson
        String json = gson.toJson(flowExecutionDTO);
        resp.getWriter().write(json);
        resp.setStatus(200);

    }

    private FlowExecution getLastExecutionForUser(List<FlowExecution> flowExecutions, String username) {
        if (flowExecutions == null  || flowExecutions.isEmpty()) {
            return null;
        }
        //search from the top of the list
        for (int i = flowExecutions.size() - 1; i >= 0; i--) {
            FlowExecution flowExecution = flowExecutions.get(i);
            if (flowExecution.getOwner().equals(username)) {
                return flowExecution;
            }
        }
        return null;
    }
}
