package modules.flow.definition.api;

import javafx.util.Pair;
import modules.step.api.DataDefinitionDeclaration;
import modules.step.api.StepDefinition;

import java.util.*;

import java.time.Duration;

public class StepUsageDeclarationImpl implements StepUsageDeclaration {

    private final StepDefinition stepDefinition;
    private  boolean skipIfFail;
    private  String stepName;
    private  String stepNameAlias;
    private static int timeUsage;
    private static double avgTime;//in ms
    private boolean isCustom = false;
    Map<String,String> inputFromNameToAlias; //<name,alias>
    Map<String,String> outputFromNameToAlias; //<name,alias>

    public StepUsageDeclarationImpl(StepDefinition stepDefinition) {
        this(stepDefinition, false, stepDefinition.name());
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
    }
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
    public void addToMapOfInput(String name,String alias){inputFromNameToAlias.put(name,alias);}
    @Override
    public void addToMapOfOutput(String name,String alias){outputFromNameToAlias.put(name,alias);}
    @Override
    public void isCustomMapping(boolean bool){isCustom = bool;}
   @Override
    public boolean getIsCustomMapping(){return isCustom;}
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
        avgTime = (avgTime * timeUsage + time.toMillis()) / (timeUsage + 1);
        return avgTime;
    }
    @Override
    public int getTimeUsed() {
        return timeUsage;
    }

    @Override
    public void addUsage() {
        timeUsage++;
    }

    public String getStepName() {
        return stepName;
    }
    public String getStepNameAlias() {
        return stepNameAlias;
    }
    public void setSkipIfFail(boolean skipIfFail) {this.skipIfFail = skipIfFail;}

    public void setStepNameAlias(String stepNameAlias) {
        this.stepNameAlias = stepNameAlias;
        stepName = stepNameAlias;
    }
    @Override
    public String getFinalStepName() {return stepName;}

    @Override
    public StepDefinition getStepDefinition() {return stepDefinition;}
    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
    @Override
    public void setFinalName(String name){this.stepName = name;}
}
