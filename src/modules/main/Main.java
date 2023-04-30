package modules.main;

import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;
import modules.step.StepDefinitionRegistry;
public class Main {
    
    public static void main(String[] args) {

//        FlowDefinition flow1 = new FlowDefinitionImpl("Flow 1", "Real flow");
//
//
//        flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.COLLECT_FILES_IN_FOLDER.getStepDefinition()));
//        flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILES_CONTENT_EXTRACTOR.getStepDefinition()));
//        flow1.validateFlowStructure();
//
//
//        FLowExecutor fLowExecutor = new FLowExecutor();
//        FlowExecution flowTestExecution1 = new FlowExecution("19",flow1);

//        fLowExecutor.executeFlow(flowTestExecution1);
       /* FLowExecutor fLowExecutor = new FLowExecutor();


*/
        /*

        FlowExecution flow2Execution1 = new FlowExecution("1", flow2);
        //todo collect all user inputs and store them on the flow execution object (maybe done inside step??)
        fLowExecutor.executeFlow(flow2Execution1);

        FlowExecution flow2Execution2 = new FlowExecution("2", flow2);
        //todo collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);
        */
        System.out.println("Hello World!");
    }
}
