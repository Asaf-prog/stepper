package modules.flow.definition.api;

import modules.step.api.DataDefinitionDeclaration;

import java.util.List;

public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();

    void validateFlowStructure();
    List<DataDefinitionDeclaration> getFlowFreeInputs();
}
