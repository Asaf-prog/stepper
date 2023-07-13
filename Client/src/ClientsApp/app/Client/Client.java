package ClientsApp.app.Client;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public class Client {
    public Client(String name, List<FlowDefinitionImpl> flows, boolean isExist){
        this. name = name;
        this.flows= flows;
        this.isExist = isExist;
    }
    String name;
    List<FlowDefinitionImpl> flows;
    boolean isExist; // check from the server if the admin is in the application
    public boolean getIsExist(){
        return isExist;
    }
    public List<FlowDefinitionImpl> getFlows(){
        return flows;
    }
}
