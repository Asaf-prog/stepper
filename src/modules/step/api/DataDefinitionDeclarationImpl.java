package modules.step.api;


import modules.dataDefinition.api.DataDefinition;

import java.io.Serializable;

public class DataDefinitionDeclarationImpl implements DataDefinitionDeclaration, Serializable {

    private final String name;
    private String finalName;
    private final DataNecessity necessity;
    private final String userString;
    private final DataDefinition dataDefinition;

    public DataDefinitionDeclarationImpl(String name, DataNecessity necessity, String userString, DataDefinition dataDefinition) {
        this.name = name;
        this.necessity = necessity;
        this.userString = userString;
        this.dataDefinition = dataDefinition;
        finalName = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataNecessity necessity() {
        return necessity;
    }

    @Override
    public String getUserString() {
        return userString;
    }

    @Override
    public DataDefinition dataDefinition() {
        return dataDefinition;
    }
    @Override
   public String getFinalName(){return finalName;}
    @Override
    public void setFinalName(String finalName){
        finalName = finalName;
    }

    @Override
    public boolean isMandatory() {
        return necessity == DataNecessity.MANDATORY;
    }

}
