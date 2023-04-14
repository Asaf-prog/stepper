package modules.flow.definition.api;
import modules.step.api.DataDefinitionDeclaration;
import java.util.ArrayList;
import java.util.List;

public class FlowDefinitionImpl implements FlowDefinition {

    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private final List<StepUsageDeclaration> steps;

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public void addFlowOutput(String outputName) {
        flowOutputs.add(outputName);
    }

    @Override
    public void validateFlowStructure() {
        // do some validation logic...
    }

    @Override
    public List<DataDefinitionDeclaration> getFlowFreeInputs() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<StepUsageDeclaration> getFlowSteps() {
        return steps;
    }

    @Override
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }
}
