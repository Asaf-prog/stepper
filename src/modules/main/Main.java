package modules.main;

import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;
import modules.step.StepDefinitionRegistry;
public class Main {
    
    //todo: 1. create a flow test
    //todo: 2. make the UI
    //todo: 3. add all summer lines
    //todo: 4. asaf eating dick 8=====D
    public static void main(String[] args) {

        FlowDefinition flow1 = new FlowDefinitionImpl("Flow 1", "Real flow");

        //flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.HELLO_WORLD.getStepDefinition()));
        //flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.COLLECT_FILES_IN_FOLDER.getStepDefinition()));

        flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.COLLECT_FILES_IN_FOLDER.getStepDefinition()));
        //flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILES_DELETER.getStepDefinition()));
        flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILES_RENAMER.getStepDefinition()));
        //flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILES_RENAMER.getStepDefinition()));
        flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.CSV_EXPORTER.getStepDefinition()));
        //flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILE_DUMPER.getStepDefinition()));
        flow1.validateFlowStructure();


        FLowExecutor fLowExecutor = new FLowExecutor();
        FlowExecution flowTestExecution1 = new FlowExecution("19",flow1);

        fLowExecutor.executeFlow(flowTestExecution1);
       /* FLowExecutor fLowExecutor = new FLowExecutor();

        FlowDefinition flowtest = new FlowDefinitionImpl("Flow our First flow", "our first flow");
        flowtest.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.SPEND_SOME_TIME.getStepDefinition()));
        flowtest.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILES_RENAMER.getStepDefinition()));
        flowtest.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILES_DELETER.getStepDefinition()));

        flowtest.validateFlowStructure();

        FLowExecutor fLowExecutorToTest = new FLowExecutor();

        FlowExecution flowTestExecution1 = new FlowExecution("19",flowtest);

        fLowExecutorToTest.executeFlow(flowTestExecution1);
*/
        /*
       FlowDefinition flow2 = new FlowDefinitionImpl("Flow 2", "show two person details");
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.HELLO_WORLD.getStepDefinition()));
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.PERSON_DETAILS.getStepDefinition(), "Person 1 Details"));
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.PERSON_DETAILS.getStepDefinition(), "Person 2 Details"));
        flow2.getFlowFormalOutputs().add("DETAILS");
        flow2.validateFlowStructure();

        FLowExecutor fLowExecutor = new FLowExecutor();

        FlowExecution flow2Execution1 = new FlowExecution("1", flow2);
        //todo collect all user inputs and store them on the flow execution object (maybe done inside step??)
        fLowExecutor.executeFlow(flow2Execution1);

        FlowExecution flow2Execution2 = new FlowExecution("2", flow2);
        //todo collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);
        */
    }
}
