package mapper;

import javafx.util.Pair;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.mappings.InitialInputValues;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.stepper.FlowExecutionDTO;
import services.stepper.StepperDTO;
import services.stepper.flow.DataDefinitionDeclarationDTO;
import services.stepper.other.InitialInputValuesDTO;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

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
        res.setFlowOutputs(flowDefinition.getFlowFormalOutputs());
        res.setFlowOfAllStepsOutputs(flowDefinition.getFlowOfAllStepsOutputs());


        return res;
    }

    @NotNull
    public static FlowDefinitionDTO convertToFlowDefinitionDTO(FlowDefinitionImpl flowDefinition) {
        FlowDefinitionDTO res = new FlowDefinitionDTO(flowDefinition.getName(), flowDefinition.getDescription());
        res.setAvgTime(flowDefinition.getAvgTime());
        res.setReadOnly(flowDefinition.isReadOnly());
        res.setFlowOutputs(flowDefinition.getFlowFormalOutputs());
        res.setFlowOfAllStepsOutputs(flowDefinition.getFlowOfAllStepsOutputs());
        res.setFreeInputs(getFreeInputs(flowDefinition));
        //custome map,aliasing,init input,continuation
        copyFreeInputs(flowDefinition, res);
        copyInitialInputs(flowDefinition, res);
        res.setTimesUsed(flowDefinition.getTimesUsed());
        res.setStepsFromStepper(flowDefinition.getSteps());
        return res;
    }

    private static void copyFreeInputs(FlowDefinitionImpl flowDefinition, FlowDefinitionDTO res) {
        List<Pair<String, DataDefinitionDeclarationDTO>> freeInputs = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclaration> pair : flowDefinition.getFreeInputs()) {
            DataDefinitionDeclarationDTO newDD = convertToDataDefinitionDeclarationDTO(pair.getValue());
            freeInputs.add(new Pair<>(pair.getKey(), newDD));
        }
        res.setFreeInputs(freeInputs);
    }

    private static void copyInitialInputs(FlowDefinitionImpl flowDefinition, FlowDefinitionDTO res) {
        List<InitialInputValuesDTO> initialInputValuesDTOs = new ArrayList<>();
        for (InitialInputValues pair : flowDefinition.getInitialInputValuesData()) {
            InitialInputValuesDTO newDD = new InitialInputValuesDTO(pair.getInputName(), pair.getInitialValue());
            initialInputValuesDTOs.add(newDD);
        }
        res.setInitialInputValuesData(initialInputValuesDTOs);
    }

    private static List<Pair<String, DataDefinitionDeclarationDTO>> getFreeInputs(FlowDefinitionImpl flowDefinition) {
        List<Pair<String, DataDefinitionDeclarationDTO>> res = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclaration> pair : flowDefinition.getFreeInputs()) {
            DataDefinitionDeclarationDTO newDD = convertToDataDefinitionDeclarationDTO(pair.getValue());
        }
        return res;
    }

    private static DataDefinitionDeclarationDTO convertToDataDefinitionDeclarationDTO(DataDefinitionDeclaration value) {
        return new DataDefinitionDeclarationDTO( value.getName(), value.necessity(), value.getUserString(), value.dataDefinition());
    }
}
