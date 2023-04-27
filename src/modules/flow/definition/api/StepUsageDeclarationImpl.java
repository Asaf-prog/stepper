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
    List<Pair<String,String>> ListOfCustomMapping;
    Map <String,String> FlowLevelAliasInStep;

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
        ListOfCustomMapping = new ArrayList<>();
        FlowLevelAliasInStep = new HashMap<>();

    }



    @Override
    public void isCustomMapping(boolean bool){isCustom = bool;}
   @Override
    public boolean getIsCustomMapping(){return isCustom;}
    @Override
    public double getAvgTime() {
        return avgTime;
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
    public void addNewValToPairOFName(String myNameDD,String conectedDD){
        ListOfCustomMapping.add(new Pair<>(myNameDD,conectedDD));
    }

    public String getFlowLevelAliasInStep(String name){return FlowLevelAliasInStep.get(name);}

    @Override
    public void setFlowLevelAliasInStep(String keyS, String valS) {
        FlowLevelAliasInStep.put(keyS, valS);}

    @Override
    public void addAlias(String sourceData, String alias) {

        FlowLevelAliasInStep.put(sourceData, alias);
    }

    @Override
    public StepDefinition getStepDefinition() {return stepDefinition;}
    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
    @Override
    public void addAnewValOfDDThatConnectedAddToListOFPair(String target,String source){
        addNewValToPairOFName(target,source);
    }
    @Override
    public boolean thisNameOfValExistInTheListOfPair(String valueToFind) {
        for (Pair<String,String> runner : ListOfCustomMapping) {
            if (runner.getKey().equals(valueToFind)) {
                return true;
            }
        }
    return false;
    }
    @Override
    public List<Pair<String,String>> getListOfCustomMapping(){return  ListOfCustomMapping;}
    @Override
    public  String getName(){return stepName;}
    @Override
    public void setFinalName(String name){this.stepName = name;}

}
