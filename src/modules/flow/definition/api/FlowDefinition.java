package modules.flow.definition.api;

import javafx.util.Pair;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;
import modules.step.api.DataDefinitionDeclarationImpl;

import javax.xml.bind.annotation.*;
import java.time.Duration;
import java.util.List;

@XmlSeeAlso(FlowDefinitionImpl.class)
@XmlRegistry
public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclarationImpl> getFlowSteps();
    List<String> getFlowFormalOutputs();
    List<Pair<String, DataDefinitionDeclarationImpl>> getFlowFreeInputs();

    StepExecutionContext setFreeInputs(StepExecutionContext context);
    void validateFlowStructure();
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
}
