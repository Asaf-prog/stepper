package modules.flow.execution.runner;

import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.context.StepExecutionContext;
import modules.flow.execution.context.StepExecutionContextImpl;
import modules.step.api.StepResult;

public class FLowExecutor {

    public void executeFlow(FlowExecution flowExecution) {//This class implements the flow

        System.out.println("Starting execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]");

        StepExecutionContext context = new StepExecutionContextImpl(); // actual object goes here...

        // populate context with all free inputs (mandatory & optional) that were given from the user
        // (typically stored on top of the flow execution object)
        //take all the free input and initialized in the object context
        // every step have a permission to context object and from the context the steps gets the input (the dd)

        // start actual execution
        for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
            StepUsageDeclaration stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);
            System.out.println("Starting to execute step: " + stepUsageDeclaration.getFinalStepName());
            StepResult stepResult = stepUsageDeclaration.getStepDefinition().invoke(context);
            System.out.println("Done executing step: " + stepUsageDeclaration.getFinalStepName() + ". Result: " + stepResult);
            // check if you should continue etc..
        }

        System.out.println("End execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]. Status: " + flowExecution.getFlowExecutionResult());
    }
}
