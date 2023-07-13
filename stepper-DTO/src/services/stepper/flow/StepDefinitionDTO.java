package services.stepper.flow;

import modules.dataDefinition.api.DataDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StepDefinitionDTO implements Serializable {
    private final String stepName;
    private final boolean readonly;
    private final List<DataDefinitionDeclarationDTO> inputs;
    private final List<DataDefinitionDeclarationDTO> outputs;

    public StepDefinitionDTO(String stepName, boolean readonly) {
        this.stepName = stepName;
        this.readonly = readonly;
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    protected void addInput(DataDefinitionDeclarationDTO dataDefinitionDeclaration) {
        inputs.add(dataDefinitionDeclaration);
    }

    protected void addOutput(DataDefinitionDeclarationDTO dataDefinitionDeclaration) {
        outputs.add(dataDefinitionDeclaration);
    }

    public String getStepName() {
        return stepName;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public List<DataDefinitionDeclarationDTO> getInputs() {
        return inputs;
    }

    public List<DataDefinitionDeclarationDTO> getOutputs() {
        return outputs;
    }

    public String getName() {
        return stepName;
    }

    public DataDefinition getDataDefinitionByName(String DDName) {
        for (DataDefinitionDeclarationDTO dataDefinitionDeclaration : outputs) {
            if (dataDefinitionDeclaration.getName().equals(DDName)) {
                return dataDefinitionDeclaration.getDataDefinition();
            } else if (dataDefinitionDeclaration.getFinalName() != null) {
                if (dataDefinitionDeclaration.getFinalName().equals(DDName))
                    return dataDefinitionDeclaration.getDataDefinition();
            }
        }
        return null;
    }

    public DataDefinitionDeclarationDTO getDataDefinitionDeclarationByName(String DDName) {
        for (DataDefinitionDeclarationDTO dataDefinitionDeclaration : outputs) {
            if (dataDefinitionDeclaration.getName().equals(DDName)) {
                return dataDefinitionDeclaration;
            } else if (dataDefinitionDeclaration.getFinalName() != null) {
                if (dataDefinitionDeclaration.getFinalName().equals(DDName))
                    return dataDefinitionDeclaration;
            }
        }
        return null;
    }

    public DataDefinitionDeclarationDTO getDataDefinitionDeclarationByNameInputList(String DDName) {
        for (DataDefinitionDeclarationDTO dataDefinitionDeclaration : inputs) {
            if (dataDefinitionDeclaration.getName().equals(DDName)) {
                return dataDefinitionDeclaration;
            } else if (dataDefinitionDeclaration.getFinalName() != null) {
                if (dataDefinitionDeclaration.getFinalName().equals(DDName))
                    return dataDefinitionDeclaration;
            }
        }
        return null;
    }

    public DataDefinition getDataDefinitionByNameTarget(String DDName) {
        for (DataDefinitionDeclarationDTO dataDefinitionDeclaration : inputs) {
            if (dataDefinitionDeclaration.getName().equals(DDName)) {
                return dataDefinitionDeclaration.getDataDefinition();
            }
        }
        return null;
    }
}

