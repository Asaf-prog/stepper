package app.body;

import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.List;

public interface bodyControllerDefinition {
    void show();
    void setBodyController(bodyController body);
    void setFlowsDetails(List<FlowDefinitionImpl> list);
}
