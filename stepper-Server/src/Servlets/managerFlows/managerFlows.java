package Servlets.managerFlows;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.ArrayList;
import java.util.List;

public class managerFlows {
    List<FlowDefinitionImpl> flows;

    managerFlows(){
    flows = new ArrayList<>();
    }

    public void addListOfFlowToList(List<FlowDefinitionImpl> flows){

        for (FlowDefinitionImpl flow : flows) {
            this.flows.add(flow);
        }
    }
}
