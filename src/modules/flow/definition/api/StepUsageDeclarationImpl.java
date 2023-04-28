package modules.flow.definition.api;

import modules.step.api.StepDefinition;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.util.*;

import java.time.Duration;
@XmlRootElement(name = "StepUsageDeclaration")
@XmlAccessorType(XmlAccessType.FIELD)

public class StepUsageDeclarationImpl implements StepUsageDeclaration {
    @XmlElement(name = "StepDefinitionImpl")
    private final StepDefinition stepDefinition;
    @XmlElement
    private  boolean skipIfFail;
    @XmlTransient
    private  String stepName;
    @XmlAttribute
    private  String stepNameAlias;
    @XmlTransient
    private Duration totalTime;
    @XmlTransient
    private static int timesUsed;
    @XmlTransient
    private static double avgTime;//in ms
    @XmlElement
    private boolean isCustom = false;
    @XmlElement
    Map<String,String> inputFromNameToAlias; //<name,alias>
    @XmlElement
    Map<String,String> outputFromNameToAlias; //<name,alias>

    public StepUsageDeclarationImpl(){
        stepDefinition=null;
        skipIfFail=false;
        stepName=null;
        stepNameAlias=null;
        totalTime=null;
        timesUsed=0;
        avgTime=0;
        inputFromNameToAlias=null;
        outputFromNameToAlias=null;
    }

    public StepUsageDeclarationImpl(StepDefinition stepDefinition) {
        this(stepDefinition, false, stepDefinition.name());
    }
    public void setStepDuration(Duration duration){
        totalTime=duration;
        updateAvgTime(duration);
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

    public class Adapter extends XmlAdapter<StepUsageDeclarationImpl,StepUsageDeclaration> {
        public Adapter() {
            super();
        }
        @Override
        public StepUsageDeclaration unmarshal(StepUsageDeclarationImpl v) throws Exception {
            return (StepUsageDeclaration)v;
        }

        @Override
        public StepUsageDeclarationImpl marshal(StepUsageDeclaration v) throws Exception {
            return (StepUsageDeclarationImpl)v;
        }
    }
}
