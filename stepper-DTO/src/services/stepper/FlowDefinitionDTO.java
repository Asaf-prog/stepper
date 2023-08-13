package services.stepper;

import javafx.util.Pair;
import mapper.Mapper;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.mappings.InitialInputValues;
import modules.step.api.DataDefinitionDeclaration;
import services.stepper.flow.DataDefinitionDeclarationDTO;
import services.stepper.flow.StepUsageDeclarationDTO;
import services.stepper.other.ContinuationDTO;
import services.stepper.other.CustomMappingDTO;
import services.stepper.other.FlowLevelAliasDTO;
import services.stepper.other.InitialInputValuesDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FlowDefinitionDTO implements Serializable {
    private String name;
    private String description;
    private List<String> flowOutputs;
    private List<String> flowOfAllStepsOutputs;
    private List<StepUsageDeclarationDTO> steps;
    private List<CustomMappingDTO> customMappings;
    private List<FlowLevelAliasDTO> flowLevelAliases;
    private List<Pair<String, DataDefinitionDeclarationDTO>> freeInputs;
    private List<Pair<String, String>> userInputs;
    private boolean isCustomMappings;
    private int timesUsed;
    private double avgTime;
    private boolean readOnly;
    private List<ContinuationDTO> continuations;
    private List<InitialInputValuesDTO> initialInputValuesData;

    public FlowDefinitionDTO(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new ArrayList<>();
        customMappings = new ArrayList<>();
        flowLevelAliases = new ArrayList<>();
        userInputs = new ArrayList<>();
        flowOfAllStepsOutputs = new ArrayList<>();
        readOnly = true;
        timesUsed = 0;
        avgTime = 0;
        continuations = new ArrayList<>();
        initialInputValuesData = new ArrayList<>();
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFlowOutputs() {
        return flowOutputs;
    }

    public void setFlowOutputs(List<String> flowOutputs) {
        this.flowOutputs = flowOutputs;
    }

    public List<String> getFlowOfAllStepsOutputs() {
        return flowOfAllStepsOutputs;
    }

    public void setFlowOfAllStepsOutputs(List<String> flowOfAllStepsOutputs) {
        this.flowOfAllStepsOutputs = flowOfAllStepsOutputs;
    }

    public List<StepUsageDeclarationDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<StepUsageDeclarationDTO> steps) {
        this.steps = steps;
    }

    public List<CustomMappingDTO> getCustomMappings() {
        return customMappings;
    }

    public void setCustomMappings(List<CustomMappingDTO> customMappings) {
        this.customMappings = customMappings;
    }

    public List<FlowLevelAliasDTO> getFlowLevelAliases() {
        return flowLevelAliases;
    }

    public void setFlowLevelAliases(List<FlowLevelAliasDTO> flowLevelAliases) {
        this.flowLevelAliases = flowLevelAliases;
    }

    public List<Pair<String, DataDefinitionDeclarationDTO>> getFreeInputs() {
        return freeInputs;
    }

    public void setFreeInputs(List<Pair<String, DataDefinitionDeclarationDTO>> freeInputs) {
        this.freeInputs = freeInputs;
    }
    public void setFreeInputsFromStepper(List<Pair<String, DataDefinitionDeclaration>> flowFreeInputs) {
       this.freeInputs= Mapper.ConvertToFreeInputsDTO(flowFreeInputs);
    }

    public List<Pair<String, String>> getUserInputs() {
        return userInputs;
    }

    public void setUserInputs(List<Pair<String, String>> userInputs) {
        this.userInputs = userInputs;
    }

    public boolean isCustomMappings() {
        return isCustomMappings;
    }

    public void setCustomMappings(boolean customMappings) {
        isCustomMappings = customMappings;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public double getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(double avgTime) {
        this.avgTime = avgTime;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public List<ContinuationDTO> getContinuations() {
        return continuations;
    }

    public void setContinuations(List<ContinuationDTO> continuations) {
        this.continuations = continuations;
    }

    public List<InitialInputValuesDTO> getInitialInputValuesData() {
        return initialInputValuesData;
    }

    public void setInitialInputValuesData(List<InitialInputValuesDTO> initialInputValuesData) {
        this.initialInputValuesData = initialInputValuesData;
    }

    public void setStepsFromStepper(List<StepUsageDeclaration> steps) {
        for (StepUsageDeclaration step : steps) {

            StepUsageDeclarationDTO stepUsageDeclarationDTO = new StepUsageDeclarationDTO(step.getStepDefinition(), step.skipIfFail(), step.getFinalStepName());
            stepUsageDeclarationDTO.setAvgTime(step.getAvgTime());
            stepUsageDeclarationDTO.setTimesUsed(step.getTimeUsed());
           // stepUsageDeclarationDTO.setStepDuration(step.getStepDuration());
            this.steps.add(stepUsageDeclarationDTO);
        }
    }

    public Object getFlowFormalOutputs() {
        return flowOutputs;
    }

    public String getFlowFormalOutputsSize() {
        return flowOutputs.size() + "";
    }

    public  List<Pair<String, DataDefinitionDeclarationDTO>>  getFlowFreeInputs() {
        return freeInputs;
    }
    public List<InitialInputValues> getORIGINALInitialInputValuesData() {
        List<InitialInputValues> initialInputValues = new ArrayList<>();
        for (InitialInputValuesDTO initialInputValuesDTO : initialInputValuesData) {
            initialInputValues.add(new InitialInputValues(initialInputValuesDTO.getName(), initialInputValuesDTO.getValue()));
        }
        return initialInputValues;
    }
}

