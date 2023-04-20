package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

public class HelloWorldStep extends AbstractStepDefinition {

    public HelloWorldStep() {
        super("Hello World", true);

        // no inputs
        addOutput(new DataDefinitionDeclarationImpl("Test", DataNecessity.MANDATORY, "test Hello world", DataDefinitionRegistry.STRING));
        // no outputs
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {//temp invoke function
        System.out.println("Hello world !");
        context.storeDataValue("Test","Hello world do that :PersonASaffffffff");
        return StepResult.SUCCESS;
    }
}
