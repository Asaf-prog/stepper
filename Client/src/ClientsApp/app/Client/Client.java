package ClientsApp.app.Client;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public class Client {
    public Client(String name, boolean isExist){
        this. name = name;

        this.isExist = isExist;
    }
    String name;
    boolean isExist; // check from the server if the admin is in the application
    public boolean getIsExist(){
        return isExist;
    }
}
