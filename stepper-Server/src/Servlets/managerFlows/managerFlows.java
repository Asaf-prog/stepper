package Servlets.managerFlows;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.ArrayList;
import java.util.List;

public class managerFlows {// this class help us to save all the from that loaded from the xml while the server alive
    List<FlowDefinitionImpl> flows;
    managerFlows(){
    flows = new ArrayList<>();
    }
    public void addListOfFlowToList(List<FlowDefinitionImpl> flows){
        for (FlowDefinitionImpl flow : flows){
            this.flows.add(flow);
        }
    }
}
