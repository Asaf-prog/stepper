package Servlets.ClientServlets.execution;

import Servlets.userManager.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;
import mapper.Mapper;
import modules.DataManeger.DataManager;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.executionManager.ExecutionManager;
import modules.flow.execution.executionManager.tasks.ExecutionTask;
import modules.flow.execution.runner.FLowExecutor;
import modules.stepper.Stepper;
import services.stepper.FlowExecutionDTO;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "execute flow Servlet",urlPatterns = "/Client/executeFlow")
@MultipartConfig
public class ExecuteFlowServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get dataManager from context
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        //if dataManager is not null
        if (dataManager == null) {
            resp.setStatus(400);
            return;
        }
        String username = SessionUtils.getUsername(req);

        String flowName = req.getHeader("flowName");
        //get request body
        String freeInputs = req.getReader().readLine();

        TypeToken<List<Pair<String, String>>> typeToken = new TypeToken<List<Pair<String, String>>>() {
        };
        List<Pair<String, String>> freeInputsList = gson.fromJson(freeInputs, typeToken.getType());

        FlowExecution flowExecution = CreateFlowExecution(freeInputsList, flowName, dataManager,username);
        if (flowExecution == null) {//dont have premission to execute
            resp.setStatus(401);
            return;
        }
        Execute(flowExecution, dataManager);
        String id= flowExecution.getUniqueId().toString();
        resp.setHeader("flowId",id);

        resp.setStatus(200);

    }

    private void sendBackFlowExeDTO(HttpServletResponse resp, FlowExecution flowExecution) {
        FlowExecutionDTO flow=Mapper.convertToFlowExecutionDTO(flowExecution);
        String flowExecutionDTO = gson.toJson(flow,FlowExecutionDTO.class);
        try {
            resp.getWriter().write(flowExecutionDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void Execute(FlowExecution flowExecution, DataManager dataManager) {

        Stepper stepperData = dataManager.getData();
        FLowExecutor fLowExecutor = new FLowExecutor();
        ExecutionManager ExeManager = stepperData.getExecutionManager();//get the one and only ExecutionManager
        try {
            ExecutionTask task = new ExecutionTask(flowExecution.getFlowDefinition().getName(),
                    flowExecution.getUniqueId(), flowExecution, fLowExecutor);
            ExeManager.executeTask(task);
            stepperData.addFlowExecution(flowExecution);
            //add task to ServletContext


            //setProgressBar(task);
            //header.setDisableOnExecutionsHistory();
            //todo move back to on success
        } catch (Exception e) {
            throw new RuntimeException(e);
        }//finally {



    }

    private FlowExecution CreateFlowExecution(List<Pair<String, String>> freeInputsList, String flowName, DataManager dataManager, String username) {

        Stepper stepperData = dataManager.getData();
        FlowDefinitionImpl flow = stepperData.getFlowFromName(flowName);
        flow.setUserInputs(freeInputsList);


        if (userHavePermission(flow,username,dataManager)) {
        FlowExecution flowExecution = new FlowExecution(flow,username);
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");

        userManager.addFlowExecution(username, flowExecution.getUniqueId());
        //flowExecution.setUserInputs();
        return flowExecution;
        }else
            return null;
    }

    private boolean userHavePermission(FlowDefinitionImpl flow, String username, DataManager dataManager) {
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        StepperUser user = userManager.getUser(username);
        List<String> userRoles = user.getRoles();
        for (String role : userRoles) {
            if (dataManager.getRoleManager().getRoleByName(role).getFlows().contains(flow)){
                return true;
            }
        }
        return false;

    }

}
