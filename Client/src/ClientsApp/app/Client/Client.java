package ClientsApp.app.Client;

import modules.DataManeger.users.StepperUser;
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

    public Client(StepperUser updatedUser) {
        this.name = updatedUser.getUsername();
        this.isExist = true;
        this.isManager = updatedUser.getIsManager();
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

    public boolean isManagerBoolean() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
