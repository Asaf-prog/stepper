package modules.flow.definition.api;

import modules.step.api.StepDefinition;
import modules.step.api.StepResult;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import java.time.Duration;

public class StepUsageDeclarationImpl implements StepUsageDeclaration , Serializable {

    private final StepDefinition stepDefinition;
    private  boolean skipIfFail;
    private  String stepName;
    private  String stepNameAlias;
    private Duration totalTime;
    private  int timesUsed;
    private  double avgTime;//in ms
    private boolean isCustom = false;
    Map<String,String> inputFromNameToAlias; //<name,alias>
    Map<String,String> outputFromNameToAlias; //<name,alias>
    private StepResult stepResult;
    Map<String,String> inputFromAliasToName; //<alias,name>
    Map<String,String> outputFromAliasToName; //<alias,name>


    public void setStepResult(StepResult stepResult){
        this.stepResult=stepResult;
    }
    public StepResult getStepResult(){
        return stepResult;
    }



    public StepUsageDeclarationImpl(StepDefinition stepDefinition) {
        this(stepDefinition, false, stepDefinition.name());
    }
    public void setStepDuration(Duration duration){
        totalTime=duration;
        updateAvgTime(duration);
    }
    @Override
    public Duration getTotalTime(){
        return totalTime;
    }


    public Instant startStepTimer(){
        return Instant.now();
    }
    public Instant stopStepTimer(){
       return Instant.now();
    }
    public StepUsageDeclarationImpl(StepDefinition stepDefinition, String name) {
        this(stepDefinition, false, name);
    }
    public StepUsageDeclarationImpl(StepDefinition stepDefinition, boolean skipIfFail, String stepName ) {

        this.stepDefinition = stepDefinition;
        this.skipIfFail = skipIfFail;
        this.stepName = stepName;
        this.stepNameAlias = stepName;
        inputFromNameToAlias = new HashMap<>();
        outputFromNameToAlias = new HashMap<>();
        timesUsed = 0;
        avgTime = 0;
        stepResult = null;

        inputFromAliasToName = new HashMap<>();
        outputFromAliasToName = new HashMap<>();

    }
    @Override
    public void addToMapOfInputFromAliasToName(String name,String alias){
        inputFromAliasToName.put(alias,name);
    }
    @Override
    public void addToMapOfOutputFromAliasToName(String name,String alias){
        outputFromAliasToName.put(alias,name);
    }
    @Override
    public Map<String,String> getInputFromNameToAlias(){return inputFromNameToAlias;}
    @Override
    public Map<String,String> getOutputFromNameToAlias(){return outputFromNameToAlias;}
    @Override
    public String getByKeyFromInputMap(String key){return inputFromNameToAlias.get(key);}
   @Override
    public String getByKeyFromOutputMap(String key){return outputFromNameToAlias.get(key);}
    @Override
    public boolean thisValueExistInTheMapInput(String valToCheck){
        if (inputFromNameToAlias.containsKey(valToCheck))
            return true;
        else
            return false;
    }
    @Override
    public boolean thisValueExistInTheMapOutput(String valToCheck){
        if (outputFromNameToAlias.containsKey(valToCheck))
            return true;
        else
            return false;
    }
    @Override
    public void addToMapOfInput(String name,String alias){
        inputFromNameToAlias.put(name,alias);}
    @Override
    public void addToMapOfOutput(String name,String alias){
        outputFromNameToAlias.put(name,alias);}
    @Override
    public void isCustomMapping(boolean bool){
        isCustom = bool;}
   @Override
    public boolean getIsCustomMapping(){
        return isCustom;}
    @Override
    public double getAvgTime() {
        return avgTime;
    }

    @Override
    public void addAlias(String name, String alias) {
        if (inputFromNameToAlias.containsKey(name))
            inputFromNameToAlias.put(name,alias);
        else if (outputFromNameToAlias.containsKey(name))
            outputFromNameToAlias.put(name,alias);

    }

    @Override
    public double updateAvgTime(Duration time) {
        avgTime = (avgTime * timesUsed + time.toMillis()) / (timesUsed + 1);
        return avgTime;
    }
    @Override
    public int getTimeUsed() {
        return timesUsed;
    }

    @Override
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
    @Override
    public String getFinalStepName() {
        return stepName;
    }

    @Override
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
    @Override
    public void setFinalName(String name){
        this.stepName = name;
    }
    @Override
    public boolean isExistInMapInputFromAliasToName (String key){
        String check = inputFromAliasToName.get(key);
        if (check != null)
            return true;
        else
            return false;
    }
    @Override
    public boolean isExistOutputFromAliasToName(String key){
        String check = outputFromAliasToName.get(key);
        if (check != null)
            return true;
        else
            return false;
    }
    public String getValueOfSourceNameByNameOfAliasFromOutputs(String key){
        return outputFromAliasToName.get(key);
    }
    public String getValueOfSourceNameByNameOfAliasFromInputs(String key){
        return inputFromAliasToName.get(key);
    }
}
