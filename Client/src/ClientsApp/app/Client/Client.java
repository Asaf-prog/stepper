package ClientsApp.app.Client;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public class Client {

    String name;
    boolean isExist; // check from the server if the admin is in the application
    boolean isManager=false;
    public Client(String name, boolean isExist){
        this. name = name;
        this.isExist = isExist;
    }
    public String getName() {
        return name;
    }
    public boolean getIsExist(){
        return isExist;
    }

    public String isManager() {
        return String.valueOf(isManager);
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
