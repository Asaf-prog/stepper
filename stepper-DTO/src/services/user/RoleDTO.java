package services.user;

import mapper.Mapper;
import modules.DataManeger.Role;
import modules.flow.definition.api.FlowDefinitionImpl;
import services.stepper.FlowDefinitionDTO;

import java.util.ArrayList;
import java.util.List;

public class RoleDTO {
        private String name;
        private List<FlowDefinitionDTO> flows;

        public RoleDTO(String name, List<FlowDefinitionImpl> flows) {
            List<FlowDefinitionDTO> flowDefinitionDTOS = new ArrayList<>();
            for (FlowDefinitionImpl flowDefinition : flows) {
                flowDefinitionDTOS.add(Mapper.convertToFlowDefinitionDTO(flowDefinition));
            }
            this.name = name;
            this.flows = flowDefinitionDTOS;

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
