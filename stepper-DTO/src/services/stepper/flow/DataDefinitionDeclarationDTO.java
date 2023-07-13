package services.stepper.flow;

import modules.dataDefinition.api.DataDefinition;
import modules.step.api.DataNecessity;

import java.io.Serializable;

public class DataDefinitionDeclarationDTO implements Serializable {
    private final String name;
    private String finalName;
    private final DataNecessity necessity;
    private final String userString;
    private final DataDefinition dataDefinition;

    public DataDefinitionDeclarationDTO(String name, DataNecessity necessity, String userString, DataDefinition dataDefinition) {
        this.name = name;
        this.necessity = necessity;
        this.userString = userString;
        this.dataDefinition = dataDefinition;
        finalName = name;
    }

    public String getName() {
        return name;
    }

    public DataNecessity getNecessity() {
        return necessity;
    }

    public String getUserString() {
        return userString;
    }

    public DataDefinition getDataDefinition() {
        return dataDefinition;
    }

    public String getFinalName() {
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public boolean isMandatory() {
        return necessity == DataNecessity.MANDATORY;
    }
}
