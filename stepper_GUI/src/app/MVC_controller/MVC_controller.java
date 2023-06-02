package app.MVC_controller;

import app.body.bodyController;
import app.header.headerController;
import app.management.mainController;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.executionManager.ExecutionManager;
import modules.flow.execution.executionManager.tasks.ExecutionTask;
import modules.flow.execution.runner.FLowExecutor;

import java.util.List;

import static modules.DataManeger.DataManager.stepperData;

public class MVC_controller {
    private mainController main;
    private  headerController header;
    private bodyController body;
    List<Pair<String,String>> freeInputs;
    private GuiAdapter guiAdapter;

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
            ExecutionTask task = new ExecutionTask(flowTestExecution.getFlowDefinition().getName(),flowTestExecution.getUniqueId() , flowTestExecution, fLowExecutor);
            setProgressor(task);
            ExeManager.executeTask(task);
            //todo do some logic and update gui accordingly\
            //todo check if gui updated correctly
            header.setDisableOnExecutionsHistory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stepperData.addFlowExecution(flowTestExecution);
    }

    private void setProgressor(ExecutionTask task) {
        int nextIndex = header.getNextFreeProgress();
        ProgressBar progressBar = header.getNextProgressBar(nextIndex);
        progressBar.progressProperty().bind(task.getProgress());
        Label label = header.getNextLabel(nextIndex);
        label.setText(task.get4DigId());
       // header.addProgress(progressBar,label,nextIndex);
    }

    private GuiAdapter createGuiAdapter() {
        return new GuiAdapter(null,null);
    }

    public void setFreeInputs(List<Pair<String,String>> freeInputs){
        this.freeInputs = freeInputs;
    }
    public List<Pair<String,String>> getFreeInputs(){
        return  freeInputs;
    }
}
