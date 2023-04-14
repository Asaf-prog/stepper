package modules.step.api;


import modules.dataDefinition.api.DataDefinition;

public interface DataDefinitionDeclaration {
    String getName();
    DataNecessity necessity();
    String userString();
    DataDefinition dataDefinition();
}
