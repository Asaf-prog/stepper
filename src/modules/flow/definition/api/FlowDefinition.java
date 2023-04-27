package modules.flow.definition.api;

import javafx.util.Pair;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.execution.context.StepExecutionContext;
import modules.flow.execution.getNameFromAliasStep.getNameFromAliasImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.time.Duration;
import java.util.List;

public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();
    void validateFlowStructure();
    List<Pair<String,DataDefinitionDeclaration>>  getFlowFreeInputs();
    StepExecutionContext  setFreeInputs(StepExecutionContext context);
    void createFreeInputsForCustomeMapping();
    void createFlowFreeInputs();
    void setIsCustomMappings(boolean isCustomMappings);
    boolean getIsCustomMappings();
    List<CustomMapping> getCustomMappings();

    void addUsage();

    double getAvgTime();

    double updateAvgTime(Duration time);
    void setMappingForStep();
    getNameFromAliasImpl getMappingFromNameToAlias();
    List<FlowLevelAlias> getFlowLevelAlias();

    void setFinalNames();
}
