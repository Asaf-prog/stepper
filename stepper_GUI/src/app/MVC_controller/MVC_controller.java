package app.MVC_controller;

import app.body.bodyController;
import app.header.headerController;
import app.management.mainController;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;

import static modules.DataManeger.DataManager.stepperData;

public class MVC_controller {
    private mainController main;
    private  headerController header;
    private bodyController body;
    public MVC_controller(mainController main, headerController header, bodyController body){
        this.main = main;
        this.header=header;
        this.body = body;
    }
    public void executeFlow(FlowDefinitionImpl flow) throws Exception {
        FlowExecution flowTestExecution = null;
        FLowExecutor fLowExecutor = new FLowExecutor();
        flowTestExecution = new FlowExecution(flow);
        fLowExecutor.executeFlow(flowTestExecution);
        stepperData.addFlowExecution(flowTestExecution);
    }
}
