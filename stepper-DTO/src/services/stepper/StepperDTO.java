package services.stepper;

import java.io.Serializable;
import java.util.*;

public class StepperDTO implements Serializable {
    private List<FlowExecutionDTO> flowExecutions;//all flow executions
    private List<FlowDefinitionDTO> flows;//all flows
    private Integer TPSize;//Thread pool size
    private String XmlPath = "";
    public Integer getTPSize() {
        return TPSize;
    }

    public String getXmlPath() {
        return XmlPath;
    }

    public void setXmlPath(String xmlPath) {
        XmlPath = xmlPath;
    }

    public List<FlowExecutionDTO> getFlowExecutions() {
        return flowExecutions;
    }

    public void setFlowExecutions(List<FlowExecutionDTO> flowExecutions) {
        this.flowExecutions = flowExecutions;
    }

    public FlowExecutionDTO getFlowExecutionById(UUID id) {
        Optional<FlowExecutionDTO> res = flowExecutions.stream().filter(flowExecution -> flowExecution.getUniqueId().equals(id)).findFirst();
        if (res.isPresent()) {
            return res.get();
        }
        return null;
    }

    public StepperDTO() {
        flows = new ArrayList<>();
        flowExecutions = new ArrayList<>();
    }

    public void setFlows(List<FlowDefinitionDTO> flows) {
        this.flows = flows;
    }

    public List<FlowDefinitionDTO> getFlows() {
        return flows;
    }


    public void setTPSize(Integer tpSize) {
        TPSize = tpSize;
    }
}