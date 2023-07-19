package Servlets.ClientServlets.execution;

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
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.executionManager.ExecutionManager;
import modules.flow.execution.executionManager.tasks.ExecutionTask;
import modules.flow.execution.runner.FLowExecutor;
import modules.stepper.Stepper;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        //if dataManager isnt null
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
        Execute(flowExecution, dataManager);

        //put flowExecutionDTO in dataManager
        sendBackFlowExeDTO(resp, flowExecution);
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

        FlowExecution flowExecution = new FlowExecution(flow,username);
        //flowExecution.setUserInputs();
        return flowExecution;
    }
}
