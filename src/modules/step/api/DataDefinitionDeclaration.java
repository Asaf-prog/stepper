package modules.step.api;


import modules.dataDefinition.api.DataDefinition;

public interface DataDefinitionDeclaration {
    String getName();
    String getFinalName();
    DataNecessity necessity();
    String getUserString();
    DataDefinition dataDefinition();
    void setFinalName(String name);

    boolean isMandatory();
}
