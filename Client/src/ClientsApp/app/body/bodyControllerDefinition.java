package ClientsApp.app.body;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public interface bodyControllerDefinition {
    void onLeave();
    void show();
    void setBodyController(bodyController body);
    void setFlowsDetails(List<FlowDefinitionImpl> list);
    void SetCurrentFlow(FlowDefinitionImpl flow);
}
