package services.stepper.flow;
import modules.step.api.StepDefinition;
import modules.step.api.StepResult;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class StepUsageDeclarationDTO {

    private final StepDefinitionDTO stepDefinition;
    private boolean skipIfFail;
    private String stepName;
    private String stepNameAlias;
    private Duration totalTime;
    private int timesUsed;
    private double avgTime; // in ms
    private boolean isCustom = false;
    private Map<String, String> inputFromNameToAlias; // <name, alias>
    private Map<String, String> outputFromNameToAlias; // <name, alias>
    private StepResult stepResult;
    private Map<String, String> inputFromAliasToName; // <alias, name>
    private Map<String, String> outputFromAliasToName; // <alias, name>

    public StepUsageDeclarationDTO(StepDefinition stepDefinition, boolean skipIfFail, String stepName) {
        this.stepDefinition = getDTOFromStepDefinition(stepDefinition);
        this.skipIfFail = skipIfFail;
        this.stepName = stepName;
        this.stepNameAlias = stepName;
        this.inputFromNameToAlias = new HashMap<>();
        this.outputFromNameToAlias = new HashMap<>();
        this.timesUsed = 0;
        this.avgTime = 0;
        this.stepResult = null;
        this.inputFromAliasToName = new HashMap<>();
        this.outputFromAliasToName = new HashMap<>();
    }

    private StepDefinitionDTO getDTOFromStepDefinition(StepDefinition stepDefinition) {
        StepDefinitionDTO stepDefinitionDTO = new StepDefinitionDTO(stepDefinition.getName(), stepDefinition.isReadonly());
       //todo update input output
        return stepDefinitionDTO;

    }

    public void setStepResult(StepResult stepResult) {
        this.stepResult = stepResult;
    }

    public StepResult getStepResult() {
        return stepResult;
    }

    public void setStepDuration(Duration duration) {
        totalTime = duration;
        updateAvgTime(duration);
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public Instant startStepTimer() {
        return Instant.now();
    }

    public Instant stopStepTimer() {
        return Instant.now();
    }

    public void addToMapOfInputFromAliasToName(String name, String alias) {
        inputFromAliasToName.put(alias, name);
    }
    public void addToMapOfOutputFromAliasToName(String name, String alias) {
        outputFromAliasToName.put(alias, name);
    }

    public Map<String, String> getInputFromNameToAlias() {
        return inputFromNameToAlias;
    }

    public Map<String, String> getOutputFromNameToAlias() {
        return outputFromNameToAlias;
    }


    public String getByKeyFromInputMap(String key) {
        return inputFromNameToAlias.get(key);
    }


    public String getByKeyFromOutputMap(String key) {
        return outputFromNameToAlias.get(key);
    }

    public boolean thisValueExistInTheMapInput(String valToCheck) {
        return inputFromNameToAlias.containsKey(valToCheck);
    }


    public boolean thisValueExistInTheMapOutput(String valToCheck) {
        return outputFromNameToAlias.containsKey(valToCheck);
    }


    public void addToMapOfInput(String name, String alias) {
        inputFromNameToAlias.put(name, alias);
    }

    public void addToMapOfOutput(String name, String alias) {
        outputFromNameToAlias.put(name, alias);
    }

    public void isCustomMapping(boolean bool) {
        isCustom = bool;
    }

    public boolean getIsCustomMapping() {
        return isCustom;
    }

    public double getAvgTime() {
        return avgTime;
    }

    public void addAlias(String name, String alias) {
        if (inputFromNameToAlias.containsKey(name))
            inputFromNameToAlias.put(name, alias);
        else if (outputFromNameToAlias.containsKey(name))
            outputFromNameToAlias.put(name, alias);
    }

    public double updateAvgTime(Duration time) {
        avgTime = (avgTime * timesUsed + time.toMillis()) / (timesUsed + 1);
        return avgTime;
    }

    public int getTimeUsed() {
        return timesUsed;
    }

    public void addUsage() {
        timesUsed++;
    }

    public String getStepName() {
        return stepName;
    }

    public String getStepNameAlias() {
        return stepNameAlias;
    }

    public void setSkipIfFail(boolean skipIfFail) {
        this.skipIfFail = skipIfFail;
    }

    public void setStepNameAlias(String stepNameAlias) {
        this.stepNameAlias = stepNameAlias;
        stepName = stepNameAlias;
    }

    public String getFinalStepName() {
        return stepName;
    }

    public StepDefinitionDTO getStepDefinition() {
        return stepDefinition;
    }

    public boolean skipIfFail() {
        return skipIfFail;
    }
    public void setFinalName(String name) {
        this.stepName = name;
    }


    public boolean isExistInMapInputFromAliasToName(String key) {
        return inputFromAliasToName.containsKey(key);
    }

    public boolean isExistOutputFromAliasToName(String key) {
        return outputFromAliasToName.containsKey(key);
    }

    public String getValueOfSourceNameByNameOfAliasFromOutputs(String key) {
        return outputFromAliasToName.get(key);
    }

    public String getValueOfSourceNameByNameOfAliasFromInputs(String key) {
        return inputFromAliasToName.get(key);
    }

    public void setAvgTime(double avgTime) {
        this.avgTime = avgTime;
    }
}
