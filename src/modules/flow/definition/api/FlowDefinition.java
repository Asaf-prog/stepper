package modules.flow.definition.api;

import javafx.util.Pair;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.FlowDefinitionException;

import java.time.Duration;
import java.util.List;

public interface FlowDefinition {
    String getName();

    String getDescription();

    List<StepUsageDeclaration> getFlowSteps();

    List<String> getFlowFormalOutputs();

    void validateFlowStructure() throws FlowDefinitionException;

    List<Pair<String, DataDefinitionDeclaration>> getFlowFreeInputs();

   // StepExecutionContext setFreeInputs(StepExecutionContext context);

    void createFlowFreeInputs();

    void setIsCustomMappings(boolean isCustomMappings);

    boolean getIsCustomMappings();

    List<CustomMapping> getCustomMappings();

    List<Pair<String, String>> getUserInputs();

    boolean addUserInput(DataDefinitionDeclaration data, String input);

    void addUsage();

    double getAvgTime();

    double updateAvgTime(Duration time);

    List<FlowLevelAlias> getFlowLevelAlias();

    void setFinalNames();
    void setOutputs(List<String> asList);
    List<String> getFlowOfAllStepsOutputs();
}
