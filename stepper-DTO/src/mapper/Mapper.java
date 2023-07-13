package mapper;

import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.stepper.FlowExecutionDTO;
import services.stepper.StepperDTO;

import java.util.ArrayList;
import java.util.List;

public  class Mapper {

    public static StepperDTO convertToStepperDTO(Stepper stepper) {
        StepperDTO stepperDTO = new StepperDTO();

        // Copy TPSize and XmlPath
        stepperDTO.setTPSize(stepper.getTPSize());
        stepperDTO.setXmlPath(stepper.getXmlPath());

        // Copy flowExecutions
        List<FlowExecutionDTO> flowExecutionDTOs = new ArrayList<>();
        for (FlowExecution flowExecution : stepper.getFlowExecutions()) {
            flowExecutionDTOs.add(convertToFlowExecutionDTO(flowExecution));
        }
        stepperDTO.setFlowExecutions(flowExecutionDTOs);

        // Copy flows
        List<FlowDefinitionDTO> flowDefinitionDTOs = new ArrayList<>();
        for (FlowDefinitionImpl flowDefinition : stepper.getFlows()) {
            flowDefinitionDTOs.add(convertToFlowDefinitionDTO(flowDefinition));
        }
        stepperDTO.setFlows(flowDefinitionDTOs);

        return stepperDTO;
    }

    public static FlowExecutionDTO convertToFlowExecutionDTO(FlowExecution flowExecution) {
        FlowExecutionDTO res = new FlowExecutionDTO();
        res.setFlowExecutionResult(flowExecution.getFlowExecutionResult().toString());//todo check if ok ?
        res.setLogs(flowExecution.getLogs());
        long time=flowExecution.getTotalTime().toMillis();
        res.setTotalTime(String.valueOf(time));
        res.setStartTime(flowExecution.getStartDateTime());
        res.setSummaryLines(flowExecution.getSummaryLines());
        res.setFlowDefinition(convertToFlowDefinitionAbsDTO(flowExecution.getFlowDefinition()));



        return res;
    }

    public static List<FlowDefinitionDTO> getFlowsDTO(List<FlowDefinitionImpl> flows ) {
        List<FlowDefinitionDTO> flowsDTO=new ArrayList<>();
        for (FlowDefinitionImpl flow : flows) {
            FlowDefinitionDTO toAdd = Mapper.convertToFlowDefinitionDTO(flow);
            flowsDTO.add(toAdd);
        }
        return flowsDTO;
    }
    public static FlowDefinitionDTO convertToFlowDefinitionAbsDTO(FlowDefinition flowDefinition) {
        FlowDefinitionDTO res = new FlowDefinitionDTO(flowDefinition.getName(), flowDefinition.getDescription());
        res.setAvgTime(flowDefinition.getAvgTime());
        //todo dont forget to complete the rest of the fields
        return res;
    }

    @NotNull
    public static FlowDefinitionDTO convertToFlowDefinitionDTO(FlowDefinitionImpl flowDefinition) {
        FlowDefinitionDTO res = new FlowDefinitionDTO(flowDefinition.getName(), flowDefinition.getDescription());
        res.setAvgTime(flowDefinition.getAvgTime());
        res.setReadOnly(flowDefinition.isReadOnly());
        res.setTimesUsed(flowDefinition.getTimesUsed());
        res.setStepsFromStepper(flowDefinition.getSteps());
        return res;
    }


}
