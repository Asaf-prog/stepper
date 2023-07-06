package DTO;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.ArrayList;
import java.util.List;

public class DTO {
    List<FlowDefinitionImpl> flows;
    public DTO(List<FlowDefinitionImpl> flows){
        this.flows = new ArrayList<>();
        for (FlowDefinitionImpl flow: flows){
            this.flows.add(flow);
        }
    }
    public List<FlowDefinitionImpl> getFlows(){
        return flows;
    }
    public void setFlows(List<FlowDefinitionImpl> flows) {
        this.flows = flows;
    }
}
