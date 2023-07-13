package ClientsApp.app.body.bodyInterfaces;

import ClientsApp.app.Client.Client;
import ClientsApp.app.body.bodyController;
import modules.flow.definition.api.FlowDefinitionImpl;
import services.stepper.FlowDefinitionDTO;

import java.util.List;

public interface bodyControllerDefinition {
    void onLeave();
    void show();
    void setBodyController(bodyController body);
    void setFlowsDetails(List<FlowDefinitionDTO> list);
    void SetCurrentFlow(FlowDefinitionDTO flow);
    void setClient(Client client);
}
