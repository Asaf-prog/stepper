package modules.flow.definition.api;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;
import java.time.Duration;
import java.util.*;

import javafx.util.Pair;
import modules.step.api.DataDefinitionDeclarationImpl;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlRootElement(name = "flow-Definition")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlowDefinitionImpl implements FlowDefinition {
    //maybe add another duration for calc
    @XmlAttribute
    protected final String name;
    @XmlElement
    protected  List<CustomMapping> customMappings;
    @XmlElement
    protected final String description;
    @XmlElement
    protected final List<String> flowOutputs;
    @XmlElement(name = "stepUsageDeclarationImpl")
    protected final List<StepUsageDeclarationImpl> steps;
    @XmlElement(name = "flowLevelAlias")
    protected  List<FlowLevelAlias> flowLevelAliases;
    @XmlElement
    protected List<Pair<String, DataDefinitionDeclarationImpl>> freeInputs;
    @XmlElement
    protected List<Pair<String,String>> userInputs;
    @XmlElement
    protected boolean isCustomMappings;
    @XmlElement
    protected static int timesUsed;
    @XmlElement
    protected static double avgTime;
    @XmlElement
    protected boolean readOnly;
    /////////////////
    public FlowDefinitionImpl() {
        name = null;
        description = null;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new ArrayList<>();
        customMappings = new ArrayList<>();
        flowLevelAliases = new ArrayList<>();
        userInputs=new ArrayList<>();
        readOnly=true;
        timesUsed=0;
        avgTime=0;
    }

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new ArrayList<>();
        customMappings = new ArrayList<>();
        flowLevelAliases = new ArrayList<>();
        userInputs=new ArrayList<>();
        readOnly=true;
        timesUsed=0;
        avgTime=0;
    }
    public static void setTimesUsed(int timesUsed) {FlowDefinitionImpl.timesUsed = timesUsed;}
    public static void setAvgTime(double avgTime) {FlowDefinitionImpl.avgTime = avgTime;}
    public List<Pair<String, String>> getUserInputs(){
        return userInputs;
    }

    @Override
    public boolean addUserInput(DataDefinitionDeclaration data,String input) {
        if (userInputs == null) {
            userInputs = new ArrayList<>();
        }
        userInputs.add(new Pair<String,String>(data.getFinalName(),input));
        return true;
    }
    @Override
    public List<FlowLevelAlias> getFlowLevelAlias(){
        return flowLevelAliases;
    }
    public void setFlowLevelAliases(List<FlowLevelAlias> flowLevelAliases) {
        this.flowLevelAliases = flowLevelAliases;
    }

    public static int getTimesUsed() {
        return timesUsed;
    }
    @Override
    public void addUsage() {
            FlowDefinitionImpl.timesUsed++;
    }

    @Override
    public double getAvgTime() {return avgTime;}

    @Override
    public double updateAvgTime(Duration time) {
        avgTime = (avgTime * (timesUsed-1) + time.toMillis()) / timesUsed;
        return avgTime;
    }
    public StepUsageDeclaration getStepByName(String name){
        for(StepUsageDeclaration tempStep: steps){
            if(tempStep.getFinalStepName().equals(name)){
                return tempStep;
            }
        }
        return null;
    }
    public void setIsCustomMappings(boolean isCustomMappings){this.isCustomMappings = isCustomMappings;}
    @Override
    public boolean getIsCustomMappings(){return isCustomMappings;}

    public void addFlowOutput(String outputName) {flowOutputs.add(outputName);}

    @Override
    public void validateFlowStructure() {
        //unique output for step
        
    }
    @Override
    public List<Pair<String, DataDefinitionDeclarationImpl>> getFlowFreeInputs() {return freeInputs;}
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<StepUsageDeclarationImpl> getFlowSteps() {
        return steps;
    }

    @Override
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }
    public boolean stepExistInListOFCustomMapping(String step) {
        for (CustomMapping tempCustomMapping : customMappings) {
            if (tempCustomMapping.getTarget().equals(step))
                return true;
        }
        return false;
    }
    public void createFlowFreeInputs() {
        List<String> listInputs = new ArrayList<>();
        for (StepUsageDeclaration step: steps) {

            List<DataDefinitionDeclarationImpl> inputListOfDD = step.getStepDefinition().inputs();

            for (DataDefinitionDeclarationImpl inputDD : inputListOfDD) {

                if (!(listInputs.contains(step.getByKeyFromInputMap(inputDD.getName()))) &&
                        !(theirIsInputFromCustomMapping(step, listInputs, step.getByKeyFromInputMap(inputDD.getName())))) {

                    listInputs.add(step.getByKeyFromInputMap(inputDD.getName()));
                    freeInputs.add(new Pair<>(step.getByKeyFromInputMap(inputDD.getName()), inputDD));
                }
            }
            List<DataDefinitionDeclarationImpl> outPutList = step.getStepDefinition().outputs();
            for (DataDefinitionDeclaration output : outPutList) {
                listInputs.add(step.getByKeyFromOutputMap(output.getName()));
            }
        }
    }
    public boolean theirIsInputFromCustomMapping(StepUsageDeclaration step, List<String> listInputs , String nameToFind){
            for (CustomMapping custom : customMappings){
                if (custom.getTarget().equals(step.getFinalStepName())){
                    String findSource = custom.getSourceData();
                    if (!custom.getTargetData1().equals(nameToFind))
                        return false;
                    for (String input: listInputs){
                        if (findSource.equals(input))
                            return true;
                    }
                }
            }
            return false;
    }
    public StepExecutionContext setFreeInputs(StepExecutionContext context) {

        System.out.println("Please fill the free inputs\n");
        Scanner myScanner = new Scanner(System.in);
        String dataToStore;
        for (Pair<String,DataDefinitionDeclarationImpl> pairOfStringAndDD : freeInputs) {
            System.out.println("The Step is: "+pairOfStringAndDD.getKey() +" The DD is: " +
                    pairOfStringAndDD.getValue().getFinalName() + " The Necessity is " + pairOfStringAndDD.getValue().necessity()
                    + " Please enter a " + pairOfStringAndDD.getValue().dataDefinition().getName());

            if (pairOfStringAndDD.getValue().getName() == "LINE"){
                int num = myScanner.nextInt();
                context.storeDataValue(pairOfStringAndDD.getValue().getFinalName(),num);
            }else {
                dataToStore = myScanner.nextLine();
                if (!dataToStore.isEmpty()) {
                    context.storeDataValue(pairOfStringAndDD.getValue().getFinalName(),dataToStore);
                }
            }
        }
        return context;
    }
    public List<String> getFlowOutputs() {return flowOutputs;}
    public List<StepUsageDeclarationImpl> getSteps() {return steps;}
    @Override
    public List<CustomMapping> getCustomMappings() {return customMappings;}
    public void setCustomMappings(List<CustomMapping> customMappings) {
        this.customMappings = customMappings;
    }
    public List<FlowLevelAlias> getFlowLevelAliases() {
        return flowLevelAliases;
    }
    public List<Pair<String, DataDefinitionDeclarationImpl>> getFreeInputs() {
        return freeInputs;
    }
    public void setFreeInputs(List<Pair<String, DataDefinitionDeclarationImpl>> freeInputs) {
        this.freeInputs = freeInputs;
    }
    public void setReadOnlyState() {
        for (StepUsageDeclaration step: steps) {
            if (step.getStepDefinition().isReadonly() == false){
                readOnly = false;
            }
        }
    }
    public boolean IsReadOnly() {
        return readOnly;
    }

   public void setFinalNames(){
        //going through all the Aliases and then custom mappings and setting the final names
        setAliases();
   }
    public void setAliases() {
        for (FlowLevelAlias alias: flowLevelAliases) {
            StepUsageDeclarationImpl step = getStepByNameFromSteps(alias.getSource(), steps);
            for (DataDefinitionDeclaration DD : step.getStepDefinition().inputs()) {
                if (DD.getName().equals(alias.getSourceData())) {
                    DD.setFinalName(alias.getAlias());
                }
            }
            for (DataDefinitionDeclaration DD : step.getStepDefinition().outputs()) {
                if (DD.getName().equals(alias.getSourceData())) {
                    DD.setFinalName(alias.getAlias());
                }
            }
        }
    }
    private StepUsageDeclarationImpl getStepByNameFromSteps(String source, List<StepUsageDeclarationImpl> steps) {

        for (StepUsageDeclarationImpl step: steps) {
            if (step.getFinalStepName().equals(source))
                return step;
        }
        return null;
    }
    static class Adapter extends XmlAdapter<FlowDefinitionImpl,FlowDefinition> {
        public Adapter() { super(); }
        public FlowDefinition unmarshal(FlowDefinitionImpl v) { return v; }
        public FlowDefinitionImpl marshal(FlowDefinition v) { return (FlowDefinitionImpl)v; }
    }
}
