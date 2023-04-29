package modules.flow.definition.api;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;

import javafx.util.Pair;
import modules.stepper.FlowDefinitionException;
import modules.stepper.FlowDefinitionExceptionItems;

public class FlowDefinitionImpl implements FlowDefinition, Serializable {

    protected final String name;
    protected final String description;
    protected List<String> flowOutputs;//list of what the user ask to get after the flow!
    List<String> flowOfAllStepsOutputs;//list of all the outputs of all the steps in the flow

    protected final List<StepUsageDeclaration> steps;
    protected List<CustomMapping> customMappings;
    protected  List<FlowLevelAlias> flowLevelAliases;
    protected List<Pair<String,DataDefinitionDeclaration>> freeInputs;
    protected List<Pair<String,String>> userInputs;
    protected boolean isCustomMappings;
    protected static int timesUsed;
    protected static double avgTime;
    protected boolean readOnly;

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new ArrayList<>();
        customMappings = new ArrayList<>();
        flowLevelAliases = new ArrayList<>();
        userInputs=new ArrayList<>();
        flowOfAllStepsOutputs= new ArrayList<>();
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
    public void validateFlowStructure() throws FlowDefinitionException {
            uniqueOutputForStep();
            checkIfMandatoryInputsAreNotUserFriendly();
            checkIfWeDoCustomMappingOnStepThatNotExist();
            checkIfWeDoCustomMappingOnDDThatNotExist();
            checkIfExistAliasForFlowStepOrDataThatNotExist();
            checkIfTheFormalOutputsExist();
    }
    public void checkIfTheFormalOutputsExist()throws FlowDefinitionException{
        if (flowOutputs.get(0).equals(""))
            return;
        for (String output:flowOutputs){
            if (!flowOfAllStepsOutputs.contains(output)){
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.THIS_IS_NOT_THE_FORMAL_OUTPUT);
            }
        }
    }
    public void checkIfExistAliasForFlowStepOrDataThatNotExist()throws FlowDefinitionException {
        for (FlowLevelAlias Alias: flowLevelAliases){
            if (!checkIfTheStepAndTheDataThatWeDoAliasExist(Alias.getSource(),Alias.getSourceData())){
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.DEFINE_ALIAS_FOR_DATA_OR_STEP_THAT_NOT_EXIST_IN_FLOW);
            }
        }
    }
    public boolean checkIfTheStepAndTheDataThatWeDoAliasExist(String stepName , String dataDefinitionName) {
        for (StepUsageDeclaration step : steps) {
            if (step.getFinalStepName().equals(stepName)) {
                List<DataDefinitionDeclaration> input = step.getStepDefinition().inputs();
                List<DataDefinitionDeclaration> outPuts = step.getStepDefinition().outputs();
                for (DataDefinitionDeclaration in : input) {
                    if (in.getName().equals(dataDefinitionName))
                        return true;
                }
                for (DataDefinitionDeclaration out : outPuts) {
                    if (out.getName().equals(dataDefinitionName))
                        return true;
                }
            }
        }
        return false;
    }
    public boolean containInList(List<DataDefinitionDeclaration>list , String dataDefinitionName){
        for (DataDefinitionDeclaration DD:list){
            if (DD.equals(dataDefinitionName))
                return true;
        }
        return false;
    }
    public void checkIfWeDoCustomMappingOnDDThatNotExist() throws FlowDefinitionException {
        for (CustomMapping custom:customMappings){
            if (!(checkIfThisDDExistInCustomMappingInListOfInputs(custom.getTargetData()) &&
                    checkIfThisDDExistInCustomMappingInListOfOutputs(custom.getSourceData()))){
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.DEFINE_CUSTOM_MAPPING_FOR_DATA_THAT_NOT_EXIST_IN_FLOW);
            }
        }
    }
    public boolean checkIfThisDDExistInCustomMappingInListOfOutputs(String name){
        for (StepUsageDeclaration step: steps){
            List<DataDefinitionDeclaration> output = step.getStepDefinition().outputs();
            for(DataDefinitionDeclaration out : output){
                if(name.equals(step.getByKeyFromOutputMap(out.getName())))
                    return true;
            }
        }
        return false;
    }

    public boolean checkIfThisDDExistInCustomMappingInListOfInputs(String name){
        for (StepUsageDeclaration step: steps){
            List<DataDefinitionDeclaration> input = step.getStepDefinition().inputs();
            for (DataDefinitionDeclaration in :input) {
                if (name.equals(step.getByKeyFromInputMap(in.getName())))
                    return true;
            }
        }
        return false;
    }
    public void checkIfWeDoCustomMappingOnStepThatNotExist() throws FlowDefinitionException {
        for(CustomMapping custom: customMappings){
            if (!(checkIfThisNameExistInStep(custom.getSource()) && checkIfThisNameExistInStep(custom.getTarget()))){
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.FLOW_USED_STEP_IN_CUSTOM_MAPPING_THAT_ARE_NOT_EXIST);
            }
        }
    }
    public boolean checkIfThisNameExistInStep(String name){
        for (StepUsageDeclaration step : steps){
            if (step.getFinalStepName().equals(name))
                return true;
        }
        return false;
    }
    public void checkIfMandatoryInputsAreNotUserFriendly() throws FlowDefinitionException {

        for (Pair<String, DataDefinitionDeclaration> inputUser : freeInputs) {
            if (!inputUser.getValue().dataDefinition().isUserFriendly()) {
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.FLOW_HAS_MANDATORY_INPUTS_THAT_ARE_NOT_USER_FRIENDLY);
            }
        }
    }
    public void uniqueOutputForStep() throws FlowDefinitionException {
        List<String> listOfOutputs = new ArrayList<>();
        for (StepUsageDeclaration step : steps) {
            List<DataDefinitionDeclaration> output = step.getStepDefinition().outputs();
            for (DataDefinitionDeclaration out : output) {
                listOfOutputs.add(step.getByKeyFromOutputMap(out.getName()));
            }
        }
        for (int i = 0; i < listOfOutputs.size(); i++) {
            String str = listOfOutputs.get(i);
            for (int j = i + 1; j < listOfOutputs.size(); j++) {
                if (str.equals(listOfOutputs.get(j))) {
                    throw new FlowDefinitionException(FlowDefinitionExceptionItems.FLOW_HAS_DUPLICATE_OUTPUTS);
                }
            }
        }
    }
    @Override
    public List<Pair<String, DataDefinitionDeclaration>> getFlowFreeInputs() {return freeInputs;}

    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<StepUsageDeclaration> getFlowSteps() {
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

            List<DataDefinitionDeclaration> inputListOfDD = step.getStepDefinition().inputs();

            for (DataDefinitionDeclaration inputDD : inputListOfDD) {

                if (!(listInputs.contains(step.getByKeyFromInputMap(inputDD.getName()))) &&
                        !(theirIsInputFromCustomMapping(step, listInputs, step.getByKeyFromInputMap(inputDD.getName())))) {

                    listInputs.add(step.getByKeyFromInputMap(inputDD.getName()));
                    freeInputs.add(new Pair<>(step.getByKeyFromInputMap(inputDD.getName()), inputDD));
                }
            }
            List<DataDefinitionDeclaration> outPutList = step.getStepDefinition().outputs();
            for (DataDefinitionDeclaration output : outPutList) {
                listInputs.add(step.getByKeyFromOutputMap(output.getName()));
            }
        }
        flowOfAllStepsOutputs = listInputs;
    }
    public boolean theirIsInputFromCustomMapping(StepUsageDeclaration step, List<String> listInputs , String nameToFind){
            for (CustomMapping custom : customMappings){
                if (custom.getTarget().equals(step.getFinalStepName())){
                    String findSource = custom.getSourceData();
                    if (!custom.getTargetData().equals(nameToFind))
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
        for (Pair<String,DataDefinitionDeclaration> pairOfStringAndDD : freeInputs) {
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
    public List<StepUsageDeclaration> getSteps() {return steps;}
    @Override
    public List<CustomMapping> getCustomMappings() {return customMappings;}
    public void setCustomMappings(List<CustomMapping> customMappings) {
        this.customMappings = customMappings;
    }
    public List<FlowLevelAlias> getFlowLevelAliases() {
        return flowLevelAliases;
    }
    public List<Pair<String, DataDefinitionDeclaration>> getFreeInputs() {
        return freeInputs;
    }
    public void setFreeInputs(List<Pair<String, DataDefinitionDeclaration>> freeInputs) {
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
            StepUsageDeclaration step = getStepByNameFromSteps(alias.getSource(), steps);
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
    private StepUsageDeclaration getStepByNameFromSteps(String source, List<StepUsageDeclaration> steps) {

        for (StepUsageDeclaration step: steps) {
            if (step.getFinalStepName().equals(source))
                return step;
        }
        return null;
    }

    public void setOutputs(List<String> asList) {
        flowOutputs = asList;
    }
}
