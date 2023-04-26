package modules.step.api;


import modules.dataDefinition.api.DataDefinition;

public interface DataDefinitionDeclaration {
    String getName();
    String getNameAfterChange();
    DataNecessity necessity();
    String userString();
    DataDefinition dataDefinition();
    void setNameForAlias(String name);
}
