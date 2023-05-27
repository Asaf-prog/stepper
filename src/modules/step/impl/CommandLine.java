package modules.step.impl;

import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.AbstractStepDefinition;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.step.api.DataNecessity;
import modules.step.api.StepResult;

import java.io.IOException;

public class CommandLine extends AbstractStepDefinition {
    public CommandLine() {
        super("Command Line", false);
         addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, " Source data", DataDefinitionRegistry.RELATION));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Result", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) throws IOException {
        return null;
    }
}
