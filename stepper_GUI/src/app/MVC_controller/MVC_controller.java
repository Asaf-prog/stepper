package app.MVC_controller;

import app.body.bodyController;
import app.header.headerController;
import app.management.mainController;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.executionManager.ExecutionManager;
import modules.flow.execution.executionManager.tasks.ExecutionTask;
import modules.flow.execution.runner.FLowExecutor;

import java.util.ArrayList;
import java.util.List;

import static modules.DataManeger.DataManager.stepperData;

public class MVC_controller {
    private mainController main;
    private  headerController header;
    private bodyController body;
    List<Pair<String,String>> freeInputs;

    public MVC_controller(mainController main, headerController header, bodyController body){
        this.main = main;
        this.header=header;
        this.body = body;
    }
    public void executeFlow(FlowDefinitionImpl flow){
        FlowExecution flowTestExecution = null;
        FLowExecutor fLowExecutor = new FLowExecutor();
        flow.setUserInputs(getFreeInputs());
        flowTestExecution = new FlowExecution(flow);
        ExecutionManager ExeManager = stepperData.getExecutionManager();//get the one and only ExecutionManager
        try {
            ExecutionTask task = new ExecutionTask(flowTestExecution.getFlowDefinition().getName(),
                    flowTestExecution.getUniqueId() , flowTestExecution, fLowExecutor);
            setProgressBar(task);
            ExeManager.executeTask(task);
            header.setDisableOnExecutionsHistory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }//finally {
        stepperData.addFlowExecution(flowTestExecution);
    }

    private void setProgressBar(ExecutionTask task) {
        int nextIndex = header.getNextFreeProgress();
        ProgressBar progressBar = header.getNextProgressBar(nextIndex);
        progressBar.setStyle("-fx-accent: #0049ff;-fx-border-radius: 25;");
        progressBar.progressProperty().bind(task.getProgress());
        task.isFailedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                progressBar.setStyle("-fx-accent: #ff2929;-fx-border-radius: 25;");
            }
        });
        task.isSuccessProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                progressBar.setStyle("-fx-accent: #00ff00;-fx-border-radius: 25;");
            }
        });
        Label label = header.getNextLabel(nextIndex);
        label.setText(task.get4DigId());
       // header.addProgress(progressBar,label,nextIndex);
    }


    public void setFreeInputs(List<Pair<String,String>> freeInputs){
        this.freeInputs = freeInputs;
    }
    public List<Pair<String,String>> getFreeInputs(){
        return  freeInputs;
    }

}
