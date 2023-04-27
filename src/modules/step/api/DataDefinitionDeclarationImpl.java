package modules.step.api;


import modules.dataDefinition.api.DataDefinition;

public class DataDefinitionDeclarationImpl implements DataDefinitionDeclaration {

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
    public String userString() {
        return userString;
    }

    @Override
    public DataDefinition dataDefinition() {
        return dataDefinition;
    }
    @Override
   public String getFinalName(){return finalName;}
    @Override
    public void setNameForAlias(String finalName){finalName = finalName;}

}
