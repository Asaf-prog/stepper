package Servlets.DTO;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.ArrayList;
import java.util.List;

public class DTO {
    List<FlowDefinitionImpl> flows;
    List<String> flowsName;
    public DTO(List<FlowDefinitionImpl> flows){
        this.flows = new ArrayList<>();
        flowsName = new ArrayList<>();
        for (FlowDefinitionImpl flow: flows){
            this.flows.add(flow);
            flowsName.add(flow.getName());
        }
    }
    public List<FlowDefinitionImpl> getFlows(){
        return flows;
    }
    public void setFlows(List<FlowDefinitionImpl> flows) {
        this.flows = flows;
    }

    public void setFlowsName(List<String> flowsName) {
        this.flowsName = flowsName;
    }
    public List<String> grtFlowsName(){
        return flowsName;
    }
}
