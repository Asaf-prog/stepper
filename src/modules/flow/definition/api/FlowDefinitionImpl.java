package modules.flow.definition.api;

import javafx.util.Pair;
import modules.mappings.*;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.FlowDefinitionException;
import modules.stepper.FlowDefinitionExceptionItems;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
    protected  int timesUsed;
    protected  double avgTime;
    protected boolean readOnly;

    //EX2
    protected List<Continuation> continuations;
    private List<FlowDefinitionImpl> flows;

    protected List<InitialInputValues> InitialInputValuesData;
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
        continuations=new ArrayList<>();
        InitialInputValuesData =new ArrayList<>();

    }
    public void setFlowOutputs(List<String> flowOutputs) {
        this.flowOutputs = flowOutputs;
    }

    public void setFlowOfAllStepsOutputs(List<String> flowOfAllStepsOutputs) {
        this.flowOfAllStepsOutputs = flowOfAllStepsOutputs;
    }

    public boolean isCustomMappings() {
        return isCustomMappings;
    }

    public void setCustomMappings(boolean customMappings) {
        isCustomMappings = customMappings;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public List<Continuation> getContinuations() {
        return continuations;
    }

    public void setContinuations(List<Continuation> continuations) {
        this.continuations = continuations;
    }

    public List<modules.mappings.InitialInputValues> getInitialInputValuesData() {
        return InitialInputValuesData;
    }

    public void setInitialInputValuesData(List<modules.mappings.InitialInputValues> initialInputValuesData) {
        InitialInputValuesData = initialInputValuesData;
    }
    @Override
    public List<String> getFlowOfAllStepsOutputs() {
        return flowOfAllStepsOutputs;
    }
    public void setUserInputs(List<Pair<String, String>> userInputs) {
        this.userInputs = userInputs;
    }
    public void setTimesUsed(int timesUsed) {this.timesUsed = timesUsed;}
    public void setAvgTime(double avgTime) {this.avgTime = avgTime;}
    public List<Pair<String, String>> getUserInputs(){
        return userInputs;
    }
    public void clearUserInputs(){
        userInputs=new ArrayList<>();
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

    public  int getTimesUsed() {
        return timesUsed;
    }
    @Override
    public void addUsage() {
            this.timesUsed++;
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
            checkIfTwoInputsOfTheSameAlias();
            uniqueOutputForStep();//4.1
            checkIfMandatoryInputsAreNotUserFriendly();//4.2
            checkIfWeDoCustomMappingOnStepThatNotExist();//4.3
            checkIfWeDoCustomMappingOnDDThatNotExist();//4.3
            checkIfExistAliasForFlowStepOrDataThatNotExist();//4.4
            checkIfTheFormalOutputsExist();//4.5
            checkIfAllConnectionsAreValid();
            //new validation for task two
            checkIfCarriedOutContinuationToFlowThatNotExist();
            checkIfWeDoContinuationToFlowWithSameTypeOfDataDefinition();
            checkIfTheInitialValueExistInFlow();// check if the input-name exist in the list of the inputs of this flows

    }
    private void checkIfCarriedOutContinuationToFlowThatNotExist() throws FlowDefinitionException {
        if (!checkContinuationCorrect()){
            String name = getNameOFTheFlowThatNotExist();
            String message = "The Flow "+ name +" is not exist.";
            throw new FlowDefinitionException(FlowDefinitionExceptionItems.THIS_FLOW_FOR_CONTINUATION_DOES_NOT_EXIST,message);
        }
    }
    private boolean checkContinuationCorrect(){
        int counter = 0;

        for (Continuation continuation:continuations) {
            for (FlowDefinitionImpl flowDefinition : this.flows) {
                if (continuation.getTargetFlow().equals(flowDefinition.getName()))
                    counter++;
            }
        }

        if (counter == continuations.size())
            return true;
        else
            return false;
    }
    private String getNameOFTheFlowThatNotExist(){
        List<String> tempName = new ArrayList<>();
        List<String> tempContinuation = new ArrayList<>();
        for (Continuation continuation:continuations) {
            tempContinuation.add(continuation.getTargetFlow());
            for (FlowDefinitionImpl flowDefinition : this.flows) {
                if (continuation.getTargetFlow().equals(flowDefinition.getName()))
                    tempName.add(continuation.getTargetFlow());
            }
        }
        for (String temp: tempName){
            if (!tempContinuation.contains(temp))
                return temp;
        }
        return null;
    }
    private void checkIfWeDoContinuationToFlowWithSameTypeOfDataDefinition() throws FlowDefinitionException {
        for (Continuation continuation:continuations) {
            for (ContinuationMapping mapping: continuation.getMappingList()){
                if (!sameType(mapping.getSourceData(),mapping.getTargetData(),continuation.getTargetFlow())){
                    String message = "The Data Definition "+ mapping.getSourceData() + " and "+ mapping.getTargetData()+ " are not in the same type";
                    throw new FlowDefinitionException(FlowDefinitionExceptionItems.THE_TARGET_AND_THE_SOURCE_ARE_NOT_IN_THE_SAME_TYPE,message);
                }
            }
        }
    }
    private boolean sameType(String sourceDD, String targetDD,String targetFlow) throws FlowDefinitionException {

        FlowDefinitionImpl targetFlowImp = getFlowByName(targetFlow);
        DataDefinitionDeclaration source =returnTheDDByNameOfOutputsDD(sourceDD);
        DataDefinitionDeclaration target= returnTheDDByNameOfInputsDD(targetDD,targetFlowImp);
        // need to check if one of them is null
        //if one of them is null check if exist in custom mapping
        if (source == null || target == null){//maybe we need to do custom mapping

            String realSourceData = getDataOfAliasFromOutputs(sourceDD);
            DataDefinitionDeclaration realSource =returnTheDDByNameOfOutputsDD(realSourceData);

            StepUsageDeclaration tempStep = getStepDataOfAliasFromFlowAndInput(targetFlowImp,targetDD);// if this step we search the source input
            if (tempStep == null)
                return false;
            String realTargetData = getDataOfAliasFromFlowAndInput(targetFlowImp,targetDD);
            DataDefinitionDeclaration realTarget =returnTheDDByNameOfInputsDDFromMap(realTargetData,tempStep);
            if (realTarget == null || realSource == null)
                return false;

            if (realSource.dataDefinition().getType().isAssignableFrom(realTarget.dataDefinition().getType()))
                return true;
            else
                return false;
        }
        else {
            if (source.dataDefinition().getType().isAssignableFrom(target.dataDefinition().getType()))
                return true;
            else {
                return false;
            }
        }
    }
    private StepUsageDeclaration getStepDataOfAliasFromFlowAndInput(FlowDefinitionImpl flowDefinition,String AliasName){
        for (StepUsageDeclaration step: flowDefinition.getSteps()){
            if (step.isExistInMapInputFromAliasToName(AliasName))
                return step;

        }
        return null;
    }
    private DataDefinitionDeclaration returnTheDDByNameOfInputsDDFromMap( String realTargetData,StepUsageDeclaration tempStep){
       return tempStep.getStepDefinition().getDataDefinitionDeclarationByNameInputList(realTargetData);
    }
    private String getDataOfAliasFromOutputs(String AliasName){
        for (StepUsageDeclaration step :steps){
            if (step.isExistOutputFromAliasToName(AliasName)){
                return step.getValueOfSourceNameByNameOfAliasFromOutputs(AliasName);
            }
        }
        return null;
    }
    private String getDataOfAliasFromFlowAndInput(FlowDefinitionImpl flowDefinition,String AliasName){
        for (StepUsageDeclaration step: flowDefinition.getSteps()){
            if (step.isExistInMapInputFromAliasToName(AliasName))
                return step.getValueOfSourceNameByNameOfAliasFromInputs(AliasName);

        }
        return null;
    }
    private DataDefinitionDeclaration returnTheDDByNameOfOutputsDD(String nameOfOutput){
        for (StepUsageDeclaration step: steps){
            DataDefinitionDeclaration temp= step.getStepDefinition().getDataDefinitionDeclarationByName(nameOfOutput);
             if (temp != null)
                 return temp;
        }
        return null;
    }
    private DataDefinitionDeclaration returnTheDDByNameOfInputsDD (String nameToSearch,FlowDefinitionImpl flowDefinition){
        for (Pair<String,DataDefinitionDeclaration> pair : flowDefinition.getFlowFreeInputs()){
            String temp = pair.getKey().toString();
            if(temp.equals(nameToSearch))
                return pair.getValue();
        }
        return null;
    }
    private FlowDefinitionImpl getFlowByName (String name){
        for (FlowDefinitionImpl flowDefinition: flows){
            if (flowDefinition.getName().equals(name))
                return flowDefinition;
        }
        return null;
    }
    private void checkIfTheInitialValueExistInFlow() throws FlowDefinitionException {
        List <String> nameOfInitialValue = new ArrayList<>();
        for (Pair<String,DataDefinitionDeclaration> pair: freeInputs){
            nameOfInitialValue.add(pair.getKey());
        }
        for (InitialInputValues data: InitialInputValuesData){
            if (!nameOfInitialValue.contains(data.getInputName())){
                String message = "The Data definition "+ data +" is not exist.";
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.THE_INIT_VALUE_ARE_NOT_EXIST,message);
            }
        }
        if (nameOfInitialValue.contains("OPERATION")){
            for (InitialInputValues data: InitialInputValuesData){
                if (data.getInputName().equals("OPERATION")){
                    if (!(data.getInitialValue().equals("ZIP")||data.getInitialValue().equals("UNZIP"))){
                        String message = "In OPERATION you must fill only ZIP or UNZIP";
                        throw new FlowDefinitionException(FlowDefinitionExceptionItems.TO_OPERATION_DATA_DEFINITION_YOU_MUST_FILL_ZIP_OR_UNZIP,message);
                    }
                }
            }
        }
    }
    private void checkIfAllConnectionsAreValid() throws FlowDefinitionException {
        for (int i = 0; i < flowLevelAliases.size(); i++) {
            FlowLevelAlias alias = flowLevelAliases.get(i);
            DataDefinitionDeclaration dd1 = findDDOutput(alias);//if not output then null
            if (dd1 != null) {//alias is output
                //go through each step and check if we have input with the same type
                for (int j = i + 1; i < flowLevelAliases.size(); i++) {
                    if(j==flowLevelAliases.size()){
                        break;
                    }
                    FlowLevelAlias alias2 = flowLevelAliases.get(j);
                    if (alias2.getAlias().equals(alias.getAlias())){
                        for (StepUsageDeclaration step : this.steps) {
                            String aliasComp = step.getInputFromNameToAlias().get(alias2.getSourceData());
                            if (aliasComp != null) {//means its exist
                                DataDefinitionDeclaration dd2 = findDDInput(alias2);
                                if (dd1.dataDefinition().getType() != dd2.dataDefinition().getType()) {
                                    String message = "The alias " + alias.getAlias() + " is used twice in two different types 1."
                                            + dd1.dataDefinition().getType().getSimpleName() + " 2." + dd2.dataDefinition().getType().getSimpleName();
                                    throw new FlowDefinitionException(FlowDefinitionExceptionItems.CANNOT_CONNECT_TWO_INPUTS_WITH_DIF_TYPES, message);
                                }
                            }
                        }
                    }

                }
            }
            else{
                DataDefinitionDeclaration dd3 = findDDOutput(alias);
                if (dd3 != null) {//alias is output
                    //go through each step and check if we have input with the same type
                    for (int j = i + 1; i < flowLevelAliases.size(); i++) {
                        if (j == flowLevelAliases.size()) {
                            break;
                        }
                        FlowLevelAlias alias2 = flowLevelAliases.get(j);
                        if (alias2.getAlias().equals(alias.getAlias())) {
                            for (StepUsageDeclaration step : this.steps) {
                                String aliasComp = step.getInputFromNameToAlias().get(alias2.getSourceData());
                                if (aliasComp != null) {//means its exist
                                    DataDefinitionDeclaration dd4 = findDDOutput(alias2);
                                    if (dd3.dataDefinition().getType() != dd4.dataDefinition().getType()) {
                                        String message = "The alias " + alias.getAlias() + " is used twice in two different types 1."
                                                + dd3.dataDefinition().getType().getSimpleName() + " 2." + dd4.dataDefinition().getType().getSimpleName();
                                        throw new FlowDefinitionException(FlowDefinitionExceptionItems.CANNOT_CONNECT_TWO_INPUTS_WITH_DIF_TYPES, message);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private DataDefinitionDeclaration findDDInput(FlowLevelAlias alias) {
        for(DataDefinitionDeclaration dd:this.getStepByName(alias.getSource()).getStepDefinition().inputs() ){
            if(dd.getFinalName().equals(alias.getSourceData())){
                return dd;
            }
        }
            return null;
    }

    private DataDefinitionDeclaration findDDOutput(FlowLevelAlias alias) {
       for(DataDefinitionDeclaration dd:this.getStepByName(alias.getSource()).getStepDefinition().outputs() ){
           if(dd.getFinalName().equals(alias.getSourceData())){
               return dd;
           }
        }
        return null;
    }

    private void checkIfTwoInputsOfTheSameAlias() throws FlowDefinitionException {
        FlowLevelAlias alias,alias2;
        for(int i=0;i<flowLevelAliases.size();i++){
            alias=flowLevelAliases.get(i);
            for(int j=i+1;j<flowLevelAliases.size();j++){
                alias2=flowLevelAliases.get(j);
                if(alias.getAlias().equals(alias2.getAlias())){
                    CheckIfBothInputs(alias,alias2);
                }
            }
        }
    }

    private void CheckIfBothInputs(FlowLevelAlias alias, FlowLevelAlias alias2) throws FlowDefinitionException {
        Class<?> type1=null,type2 = null;
        type1=IsAliasInputInStep(alias,getStepByName(alias.getSource()).getStepDefinition().inputs());//if input return his type else null
        type2=IsAliasInputInStep(alias2,getStepByName(alias2.getSource()).getStepDefinition().inputs());
       if(type1 != null && type2!=null)//both inputs with thw same name
            if(type1!=type2){//and not the same type
                String message = "The alias "+alias.getAlias()+" is used twice";
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.MORE_THEN_ONE_ALIAS_WITH_THE_SAME_NAME,message);
            }

        }

    private Class<?> IsAliasInputInStep(FlowLevelAlias alias, List<DataDefinitionDeclaration> inputs) {
        for(DataDefinitionDeclaration input:inputs){
            if(input.getName().equals(alias.getSourceData())){
                return input.dataDefinition().getType();
            }
        }
        return null;
    }

    public void checkIfTheFormalOutputsExist()throws FlowDefinitionException{
        if (flowOutputs.get(0).equals(""))
            return;
        for (String output:flowOutputs){
            if (!flowOfAllStepsOutputs.contains(output)){
                String message = "The output "+output+" does not exist";
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.THIS_IS_NOT_THE_FORMAL_OUTPUT,message);
            }
        }
    }
    public void checkIfExistAliasForFlowStepOrDataThatNotExist()throws FlowDefinitionException {
        for (FlowLevelAlias Alias: flowLevelAliases){
            if (!checkIfTheStepAndTheDataThatWeAliasDoExist(Alias.getSource(),Alias.getSourceData())){
                String stepName = checkIfTheStepExist(Alias.getSource());
                if (stepName == null){
                    String message = "The step "+Alias.getSource()+" does not exist";
                    throw new FlowDefinitionException(FlowDefinitionExceptionItems.DEFINE_ALIAS_FOR_DATA_OR_STEP_THAT_NOT_EXIST_IN_FLOW,message);
                }else{
                    String message = "The step "+Alias.getSource()+" is exist but the data definition "+ Alias.getSourceData()+" does not exist";
                    throw new FlowDefinitionException(FlowDefinitionExceptionItems.DEFINE_ALIAS_FOR_DATA_OR_STEP_THAT_NOT_EXIST_IN_FLOW,message);
                }
            }
        }
    }
    private String checkIfTheStepExist(String stepToCheck) {
        for (StepUsageDeclaration step : steps){
            if(step.getFinalStepName().equals(stepToCheck)){
                return stepToCheck;
            }
        }
        return null;
    }
    private boolean checkIfTheStepAndTheDataThatWeAliasDoExist(String stepName , String dataDefinitionName) {
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
                String DDCustomNotExist = "The custom mapping is: "+findTheDDThatNotExist(custom.getTargetData(),custom.getSourceData())+" from Step "+(custom.getSource()+".");
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.DEFINE_CUSTOM_MAPPING_FOR_DATA_THAT_NOT_EXIST_IN_FLOW,DDCustomNotExist);
            }
        }
    }
    private String findTheDDThatNotExist(String target,String source){
        for (StepUsageDeclaration step: steps){
            List<DataDefinitionDeclaration> output = step.getStepDefinition().outputs();
            for(DataDefinitionDeclaration out : output){
                if(name.equals(step.getByKeyFromOutputMap(out.getName())))
                    return source;
            }
        }
        return target;
    }
    private boolean checkIfThisDDExistInCustomMappingInListOfOutputs(String name){
        for (StepUsageDeclaration step: steps){
            List<DataDefinitionDeclaration> output = step.getStepDefinition().outputs();
            for(DataDefinitionDeclaration out : output){
                if(name.equals(step.getByKeyFromOutputMap(out.getName())))
                    return true;
            }
        }
        return false;
    }

    private boolean checkIfThisDDExistInCustomMappingInListOfInputs(String name){
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
                String customHowDoNotExist = getCustomHowDoNotExist(custom.getSource(),custom.getTarget());
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.FLOW_USED_STEP_IN_CUSTOM_MAPPING_THAT_ARE_NOT_EXIST,customHowDoNotExist);
            }
        }
    }
    private String  getCustomHowDoNotExist(String source, String target){
        if (!checkIfThisNameExistInStep(source)){
            return source;
        }
        return target;
    }
    private boolean checkIfThisNameExistInStep(String name){
        for (StepUsageDeclaration step : steps){
            if (step.getFinalStepName().equals(name))
                return true;
        }
        return false;
    }
    public void checkIfMandatoryInputsAreNotUserFriendly() throws FlowDefinitionException {
        if (this.continuations.size() == 0)
            return;
        //todo remove above and check if continuation is update flow free inputs
        for (Pair<String, DataDefinitionDeclaration> inputUser : freeInputs) {
            if (!inputUser.getValue().dataDefinition().isUserFriendly()) {
                String theUnUserFriendlyMandatoryInput = inputUser.getKey();
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.FLOW_HAS_MANDATORY_INPUTS_THAT_ARE_NOT_USER_FRIENDLY,"input name : "+theUnUserFriendlyMandatoryInput);
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
                    String dupicateOutputMessage = "There are two outputs with the same name "+str;
                    throw new FlowDefinitionException(FlowDefinitionExceptionItems.FLOW_HAS_DUPLICATE_OUTPUTS,dupicateOutputMessage);
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
        //todo ex2 sync with initial inputs that removing the user option to change the input
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
                flowOfAllStepsOutputs.add(step.getByKeyFromOutputMap(output.getName()));
                listInputs.add(step.getByKeyFromOutputMap(output.getName()));
            }
        }
        //flowOfAllStepsOutputs = listInputs;
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
    public void SetAllFlows(List<FlowDefinitionImpl> flows){
        this.flows = flows;
    }
}
