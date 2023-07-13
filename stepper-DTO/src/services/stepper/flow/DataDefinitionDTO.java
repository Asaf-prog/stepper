package services.stepper.flow;

import modules.dataDefinition.api.DataDefinition;

import java.io.Serializable;
public class DataDefinitionDTO implements Serializable {
    private String name;
    private boolean userFriendly;
    private String typeName;


    public DataDefinitionDTO(String name, boolean userFriendly, String typeName) {
        this.name = name;
        this.userFriendly = userFriendly;
        this.typeName = typeName;
    }

    public DataDefinitionDTO(DataDefinition dataDefinition) {
        this.name = dataDefinition.getName();
        this.userFriendly = dataDefinition.isUserFriendly();
        this.typeName = dataDefinition.getTypeName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUserFriendly() {
        return userFriendly;
    }

    public void setUserFriendly(boolean userFriendly) {
        this.userFriendly = userFriendly;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
