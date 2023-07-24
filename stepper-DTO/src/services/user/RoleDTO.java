package services.user;

import mapper.Mapper;
import modules.DataManeger.Role;
import modules.flow.definition.api.FlowDefinitionImpl;
import services.stepper.FlowDefinitionDTO;

import java.util.ArrayList;
import java.util.List;

public class RoleDTO {
        private String name;
        private String description;
        private List<FlowDefinitionDTO> flows;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public RoleDTO(String name, List<FlowDefinitionImpl> flows) {
            List<FlowDefinitionDTO> flowDefinitionDTOS = new ArrayList<>();
            for (FlowDefinitionImpl flowDefinition : flows) {
                flowDefinitionDTOS.add(Mapper.convertToFlowDefinitionDTO(flowDefinition));
            }
            this.name = name;
            this.flows = flowDefinitionDTOS;
            this.description = "";

        }
    public RoleDTO(String name,String description, List<FlowDefinitionImpl> flows) {
        List<FlowDefinitionDTO> flowDefinitionDTOS = new ArrayList<>();
        for (FlowDefinitionImpl flowDefinition : flows) {
            flowDefinitionDTOS.add(Mapper.convertToFlowDefinitionDTO(flowDefinition));
        }
        this.name = name;
        this.flows = flowDefinitionDTOS;
        this.description = description;

    }
    public RoleDTO(String name, String description) {
        List<FlowDefinitionDTO> flowDefinitionDTOS = new ArrayList<>();
        this.name = name;
        this.flows = flowDefinitionDTOS;
        this.description = description;

    }



    public String getName() {
            return name;
        }

        public List<FlowDefinitionDTO> getFlows() {
            return flows;
        }

    public void setFlows(List<FlowDefinitionDTO> flows) {
        this.flows = flows;
    }
}
