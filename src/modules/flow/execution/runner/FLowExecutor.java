package modules.flow.execution.runner;

import modules.DataManeger.GetDataFromXML;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.context.StepExecutionContext;
import modules.flow.execution.context.StepExecutionContextImpl;
import modules.step.api.StepResult;

import java.io.IOException;
import java.util.List;
public class FLowExecutor {
    public void executeFlow(FlowExecution flowExecution){//This class implements the flow
        StepExecutionContext context = new StepExecutionContextImpl(); // actual object goes here...

        context.setSteps(flowExecution.getFlowDefinition().getFlowSteps());
        context.setUserInputs(flowExecution);//sets user inputs into the context


        //context=flowExecution.getFlowDefinition().setFreeInputs(context);

        System.out.println("Starting execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]");
        try {
            for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
                StepUsageDeclaration stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);
                System.out.println("Starting to execute step: " + stepUsageDeclaration.getFinalStepName());
                context.setStep(stepUsageDeclaration);

                //function that inisilaized the contex with the map input and the map output

                context.setInputOfCurrentStep(stepUsageDeclaration.getInputFromNameToAlias());
                context.setOutputOfCurrentStep(stepUsageDeclaration.getOutputFromNameToAlias());

                StepResult stepResult = stepUsageDeclaration.getStepDefinition().invoke(context);
                System.out.println("Done executing step: " + stepUsageDeclaration.getFinalStepName() + ". Result: " + stepResult);
                // check if you should continue etc..
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("End execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]. Status: " + flowExecution.getFlowExecutionResult());
    }
}
