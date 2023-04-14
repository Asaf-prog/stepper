package modules.step.impl;

import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.StepResult;

public class HelloWorldStep extends AbstractStepDefinition {

    public HelloWorldStep() {
        super("Hello World", true);

        // no inputs

        // no outputs
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        System.out.println("Hello world !");
        return StepResult.SUCCESS;
    }
}
