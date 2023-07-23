package modules.DataManeger;

import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;

import java.util.List;

public class Role {
    private String name;
    private List<FlowDefinitionImpl> flows;

    public Role(String name, List<FlowDefinitionImpl> flows) {
        this.name = name;
        this.flows = flows;

    }

    public String getName() {
        return name;
    }

    public List<FlowDefinitionImpl> getFlows() {
        return flows;
    }

}
