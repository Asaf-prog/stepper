package app.MVC_controller;

import app.body.bodyController;
import app.header.headerController;
import app.management.mainController;
import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;

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
        try {
            fLowExecutor.executeFlow(flowTestExecution);
            header.setDisableOnExecutionsHistory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stepperData.addFlowExecution(flowTestExecution);
    }
    public void setFreeInputs(List<Pair<String,String>> freeInputs){
        this.freeInputs = freeInputs;
    }
    public List<Pair<String,String>> getFreeInputs(){
        return  freeInputs;
    }
}
