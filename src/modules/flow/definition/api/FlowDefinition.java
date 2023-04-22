package modules.flow.definition.api;

import javafx.util.Pair;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;

import java.util.List;

public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();

    void validateFlowStructure();
    List<Pair<String,DataDefinitionDeclaration>>  getFlowFreeInputs();
    public StepExecutionContext  setFreeInputs(StepExecutionContext context);
    public void createFlowFreeInputs();
}
