package modules.DataManeger;

import modules.flow.definition.api.FlowDefinitionImpl;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private String name;
    private List<FlowDefinitionImpl> flows;

    private String description="";

    public Role(String name, List<FlowDefinitionImpl> flows) {
        this.name = name;
        this.flows = flows;


    }
    public Role(String name, List<FlowDefinitionImpl> flows,String description) {
        this.name = name;
        this.flows = flows;
        this.description=description;


    }
    public Role(String name, String description) {
        this.name = name;
        this.flows = new ArrayList<>();
        this.description=description;

    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFlows(List<FlowDefinitionImpl> flows) {
        this.flows = flows;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<FlowDefinitionImpl> getFlows() {
        return flows;
    }

    public void setNewFlows(List<FlowDefinitionImpl> newFlows) {
        List<FlowDefinitionImpl> flows=new ArrayList<>();
        for (FlowDefinitionImpl flow:newFlows){
            flows.add(flow);
        }
        this.flows = flows;
    }
}
