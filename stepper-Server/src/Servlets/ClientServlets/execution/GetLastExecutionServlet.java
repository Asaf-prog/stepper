package Servlets.ClientServlets.execution;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import mapper.Mapper;
import modules.DataManeger.DataManager;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.executionManager.tasks.ExecutionTask;
import services.stepper.FlowExecutionDTO;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "get last execution Servlet",urlPatterns = "/Client/flowEnded")
@MultipartConfig
public class GetLastExecutionServlet extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get dataManager from context
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        String flowId = req.getHeader("flowId");
        FlowExecution execution = getLastExecutionForUser(dataManager.getStepperData().getFlowExecutions(), SessionUtils.getUsername(req),flowId);
        if (execution == null) {
          //  Double prog=setProgress(dataManager.getStepperData().getFlowExecutions(), SessionUtils.getUsername(req),flowId);
          //  ExecutionTask task=dataManager.getStepperData().getExecutionManager().getTaskById(flowId);
//todo maybe something with the progress bar

            resp.setStatus(400);
            return;
        }
        FlowExecutionDTO flowExecutionDTO = Mapper.convertToFlowExecutionDTO(execution);
        //gson
        String json = gson.toJson(flowExecutionDTO);
        resp.getWriter().write(json);
        resp.setStatus(200);

    }
        private void setProgressBar(ExecutionTask task) {
            int nextIndex =1;// header.getNextFreeProgress(); todo get with quary prams
//         //   ProgressBar progressBar = header.getNextProgressBar(nextIndex);
//            progressBar.setStyle("-fx-accent: #0049ff;-fx-border-radius: 25;");
//            progressBar.progressProperty().bind(task.getProgress());
//            task.isFailedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (newValue) {
//                    progressBar.setStyle("-fx-accent: #ff2929;-fx-border-radius: 25;");
//                }
//            });
//            task.isSuccessProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (newValue) {
//                    progressBar.setStyle("-fx-accent: #00ff00;-fx-border-radius: 25;");
//                }
//            });
//            Label label = header.getNextLabel(nextIndex);
//            label.setText(task.get4DigId());
//            // header.addProgress(progressBar,label,nextIndex);
        }


    private FlowExecution getLastExecutionForUser(List<FlowExecution> flowExecutions, String username, String flowId) {
        if (flowExecutions == null  || flowExecutions.isEmpty()) {
            return null;
        }

        //search from the top of the list
        for (FlowExecution flowExecution : flowExecutions) {
            if (flowExecution.getUniqueId().toString().equals(flowId) && flowExecution.getOwner().equals(username)) {
                if (flowExecution.isDone()) {
                    return flowExecution;
                }else{
                    //
                    return null;

                }
            }
        }
        return null;
    }
}