package modules.step.api;


import modules.dataDefinition.api.DataDefinition;

public interface DataDefinitionDeclaration {
    String getName();
    String getFinalName();
    DataNecessity necessity();
    String userString();
    DataDefinition dataDefinition();
    void setFinalName(String name);
}
