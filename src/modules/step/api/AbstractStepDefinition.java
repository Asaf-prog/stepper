package modules.step.api;

import modules.dataDefinition.api.DataDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStepDefinition implements StepDefinition , Serializable {
    private final String stepName;
    private final boolean readonly;
    private final List<DataDefinitionDeclaration> inputs;
    private final List<DataDefinitionDeclaration> outputs;

    public AbstractStepDefinition(String stepName, boolean readonly) {
        this.stepName = stepName;
        this.readonly = readonly;
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    protected void addInput(DataDefinitionDeclaration dataDefinitionDeclaration) {
        inputs.add(dataDefinitionDeclaration);
    }
    protected void addOutput(DataDefinitionDeclaration dataDefinitionDeclaration) {
        outputs.add(dataDefinitionDeclaration);
    }
    @Override
    public String name() {
        return stepName;
    }
    @Override
    public boolean isReadonly() {
        return readonly;
    }

    @Override
    public List<DataDefinitionDeclaration> inputs() {
        return inputs;
    }

    @Override
    public List<DataDefinitionDeclaration> outputs() {
        return outputs;
    }

    @Override
    public String getName() {
        return stepName;
    }
    @Override
    public DataDefinition getDataDefinitionByName(String DDName) {
        for (DataDefinitionDeclaration dataDefinitionDeclaration : outputs) {
            if (dataDefinitionDeclaration.getName().equals(DDName)) {
                return dataDefinitionDeclaration.dataDefinition();
            }
            else if(dataDefinitionDeclaration.getFinalName()!=null){
               if (dataDefinitionDeclaration.getFinalName().equals(DDName))
                   return dataDefinitionDeclaration.dataDefinition();
            }
        }
        return null;
    }
    @Override
    public DataDefinition getDataDefinitionByNameTarget(String DDName) {
        for (DataDefinitionDeclaration dataDefinitionDeclaration : inputs) {
            if (dataDefinitionDeclaration.getName().equals(DDName)) {
                return dataDefinitionDeclaration.dataDefinition();
            }
        }
        return null;
    }

}
