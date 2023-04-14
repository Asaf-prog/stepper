package modules.flow.definition.api;

import modules.step.api.StepDefinition;

public interface StepUsageDeclaration {
    String getFinalStepName();
    StepDefinition getStepDefinition();
    boolean skipIfFail();
}
