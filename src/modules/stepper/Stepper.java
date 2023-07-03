package modules.stepper;
 import modules.flow.execution.executionManager.ExecutionManager;
 import modules.mappings.*;
 import modules.flow.definition.api.FlowDefinitionImpl;
 import modules.flow.definition.api.StepUsageDeclaration;
 import modules.flow.definition.api.StepUsageDeclarationImpl;
 import modules.flow.execution.FlowExecution;
 import modules.step.StepDefinitionRegistry;
 import modules.step.api.DataDefinitionDeclaration;
 import modules.step.api.StepDefinition;
 import schemeTest2.generatepackage.*;

 import java.io.Serializable;
 import java.util.*;

public class Stepper implements Serializable {
    private List<FlowExecution> flowExecutions;//all flow executions
    private List<FlowDefinitionImpl> flows;//all flows
    private Integer TPSize;//Thread pool size
    private  String XmlPath="";

    private ExecutionManager executionManager;//handle doing execution according to the thread pool size


    public Integer getTPSize() {
        return TPSize;
    }

    public void setTPSize(Integer TPSize) throws StepperDefinitionException {
       if (TPSize<=0) {
           String message = "you enter "+ TPSize + " ,as you know it is not a positive number...";
           throw new StepperDefinitionException(StepperDefinitionExceptionItems.THE_NUMBER_OF_THREADS_IS_NOT_POSITIVE,message );
       }
           this.TPSize = TPSize;
        this.executionManager.setNumberOfThreads(TPSize);
    }

    public String getXmlPath() {
        return XmlPath;
    }

    public void setXmlPath(String xmlPath) {
        XmlPath = xmlPath;
    }
    public List<FlowExecution> getFlowExecutions() {
        return flowExecutions;
    }

    public void setFlowExecutions(List<FlowExecution> flowExecutions) {
        this.flowExecutions = flowExecutions;
    }
    public FlowExecution getFlowExecutionById(UUID id){
        Optional<FlowExecution> res = flowExecutions.stream().filter(flowExecution -> flowExecution.getUniqueId().equals(id)).findFirst();
        if(res.isPresent()){
            return res.get();
        }
        return null;

    }

    public Stepper(){
        flows = new ArrayList<>();
        flowExecutions = new ArrayList<>();
        executionManager = new ExecutionManager();
    }

    public void setFlows(List<FlowDefinitionImpl> flows) {
        this.flows = flows;
    }

    public List<FlowDefinitionImpl> getFlows() {
        return flows;
    }
    public void copyFlowFromXMLObject(STFlow stFlow) throws FlowDefinitionException {
        FlowDefinitionImpl flowToAdd = new FlowDefinitionImpl(stFlow.getName(), stFlow.getSTFlowDescription());
        List<FlowLevelAlias> flowLevelAliasesToAdd = new ArrayList<>();
        //add all steps
        for (int i = 0; i < stFlow.getSTStepsInFlow().getSTStepInFlow().size(); i++) {

            STStepInFlow currStStep=stFlow.getSTStepsInFlow().getSTStepInFlow().get(i);
            String StepName = currStStep.getName();
            String StepNameAlias = currStStep.getAlias();
            Optional<StepDefinition> checkStepExist =Optional.ofNullable(StepDefinitionRegistry.getStepDefinitionByName(StepName));
            if (!checkStepExist.isPresent())
                throw new FlowDefinitionException(FlowDefinitionExceptionItems.FLOW_HAS_STEP_THAT_DOES_NOT_EXIST,StepName);
            StepUsageDeclarationImpl declaration =new StepUsageDeclarationImpl(checkStepExist.get());


            if (StepNameAlias!=null) {
                declaration.setStepNameAlias(StepNameAlias);
            }//if step have alias set it if not set the step name as the alias
            else {
                declaration.setStepNameAlias(StepName);
                Boolean stepSkipIfFail = stFlow.getSTStepsInFlow().getSTStepInFlow().get(i).isContinueIfFailing();
                if (stepSkipIfFail == null)
                    declaration.setSkipIfFail(false);
                else
                    declaration.setSkipIfFail(stepSkipIfFail);
            }
            flowToAdd.getFlowSteps().add(declaration);
        }
        if (stFlow.getSTCustomMappings()!= null) {//this step is costume mapping
            if (stFlow.getSTCustomMappings().getSTCustomMapping().size()!=0)
                getFlowCustomMapping(stFlow, flowToAdd);
        }
        if (stFlow.getSTFlowLevelAliasing()!= null) {
            if(stFlow.getSTFlowLevelAliasing().getSTFlowLevelAlias().size()!=0)
                getFlowLvlALiasing(stFlow, flowLevelAliasesToAdd);
        }
        if (stFlow.getSTContinuations()!= null) {
            if(stFlow.getSTContinuations().getSTContinuation().size()!=0) {
                getFlowContinuations(stFlow, flowToAdd);
            }
        }
        if(stFlow.getSTInitialInputValues()!= null) {
            if (stFlow.getSTInitialInputValues().getSTInitialInputValue().size()!=0)
            {
                getFlowInitialInputs(stFlow, flowToAdd);
            }
        }
        setFormalOutputs(flowToAdd,stFlow.getSTFlowOutput());
        flowToAdd.setFlowLevelAliases(flowLevelAliasesToAdd);
        flows.add(flowToAdd);
    }

    private static void getFlowCustomMapping(STFlow stFlow, FlowDefinitionImpl flowToAdd) {
        flowToAdd.setIsCustomMappings(true);

        List<STCustomMapping> stCustomMappings = stFlow.getSTCustomMappings().getSTCustomMapping();
        List<CustomMapping> CustomMappingsToAdd = new ArrayList<>();

        for (int j = 0; j < stCustomMappings.size(); j++) {
            STCustomMapping currStCustomMapping = stCustomMappings.get(j);
            CustomMapping temp = new CustomMapping(currStCustomMapping.getSourceStep()
                    , currStCustomMapping.getSourceData(),
                    currStCustomMapping.getTargetStep()
                    , currStCustomMapping.getTargetData());
            CustomMappingsToAdd.add(j, temp);
        }
        flowToAdd.setCustomMappings(CustomMappingsToAdd);
    }

    private static void getFlowInitialInputs(STFlow stFlow, FlowDefinitionImpl flowToAdd) {
        List<InitialInputValues> toAdd=new ArrayList<>();
        List<STInitialInputValue> stInitialInputValues= stFlow.getSTInitialInputValues().getSTInitialInputValue();
        for (int k = 0; k < stInitialInputValues.size(); k++) {
            STInitialInputValue currStInitialInputValue = stInitialInputValues.get(k);
            InitialInputValues temp = new InitialInputValues(currStInitialInputValue.getInputName(),currStInitialInputValue.getInitialValue());
            toAdd.add(k, temp);
        }
        flowToAdd.setInitialInputValuesData(toAdd);
    }

    private static void getFlowContinuations(STFlow stFlow, FlowDefinitionImpl flowToAdd) {
        List<Continuation> continuationsToAdd = new ArrayList<>();
        List<STContinuation> stContinuations = stFlow.getSTContinuations().getSTContinuation();

        for (int k = 0; k < stContinuations.size(); k++) {
            STContinuation currStContinuation = stContinuations.get(k);
            Continuation temp = new Continuation();
            temp.setTargetFlow(currStContinuation.getTargetFlow());
            temp.initMappingList(currStContinuation.getSTContinuationMapping());
            continuationsToAdd.add(k, temp);
        }
        flowToAdd.setContinuations(continuationsToAdd);
    }

    private static void getFlowLvlALiasing(STFlow stFlow, List<FlowLevelAlias> flowLevelAliasesToAdd) {
        //adding FlowLevelAlias for each step

        List<STFlowLevelAlias> stFlowLevelAliases = stFlow.getSTFlowLevelAliasing().getSTFlowLevelAlias();
        for (int k = 0; k < stFlowLevelAliases.size(); k++) {
            STFlowLevelAlias currStFlowLevelAlias = stFlowLevelAliases.get(k);
            FlowLevelAlias temp = new FlowLevelAlias(currStFlowLevelAlias.getStep()
                    , currStFlowLevelAlias.getSourceDataName()
                    , currStFlowLevelAlias.getAlias());
            flowLevelAliasesToAdd.add(k, temp);
        }
    }

    private void setFormalOutputs(FlowDefinitionImpl flowToAdd, String stFlowOutput) {
        flowToAdd.setOutputs(Arrays.asList(stFlowOutput.split(",")));
    }

    public void validateStepper() throws StepperDefinitionException, FlowDefinitionException {
        for (FlowDefinitionImpl flowDefinition : flows) {
            initializedInputAndOutput(flowDefinition.getSteps(), flowDefinition.getFlowLevelAliases());//automatic mapping
            this.updateAliasesPerStep();
            flowDefinition.setFinalNames();
            flowDefinition.createFlowFreeInputs();//including one for user input
            flowDefinition.setReadOnlyState();
        }
        //Stepper Validate
        validateFlowsUniqueName();
        for (FlowDefinitionImpl flow : flows) {
            flow.SetAllFlows(flows);
            flow.validateFlowStructure();
        }
    }

    private void validateFlowsUniqueName() throws StepperDefinitionException {
            for (int i = 0; i < flows.size(); i++) {
                for (int j = i + 1; j < flows.size(); j++) {
                    if (flows.get(i).getName().equals(flows.get(j).getName())) {
                        throw new StepperDefinitionException(StepperDefinitionExceptionItems.FLOWS_NOT_UNIQUE,"Flow name: "+flows.get(i).getName());
                    }
                }
            }
    }
    //this function we create in StepUsageDeclaration a map from the real name to the alias name.
    //if  to this there is no alias the map will be from the real name to real name (map<String,String>(real_name,real_name).
    public void initializedInputAndOutput(List<StepUsageDeclaration> stepOfFlow, List<FlowLevelAlias> flowLevelAliases){
        for (StepUsageDeclaration step: stepOfFlow){
            for (FlowLevelAlias alias: flowLevelAliases){
                if (step.getFinalStepName().equals(alias.getSource())){
                    if (checkIfNameFoundInInput(step,alias.getSourceData())){
                        step.addToMapOfInput(alias.getSourceData(), alias.getAlias());
                        step.addToMapOfInputFromAliasToName(alias.getSourceData(), alias.getAlias());
                    }
                    else {//is belonging to output map
                        step.addToMapOfOutput(alias.getSourceData(), alias.getAlias());
                        step.addToMapOfOutputFromAliasToName(alias.getSourceData(), alias.getAlias());
                    }
                }
            }
        }
        for (StepUsageDeclaration step: stepOfFlow){
            List<DataDefinitionDeclaration> inputs =  step.getStepDefinition().inputs();
            for (DataDefinitionDeclaration input:inputs){
                if (!thisInputExistInTheMapOfInput(step,input.getName())){
                    step.addToMapOfInput(input.getName(), input.getName());
                    step.addToMapOfInputFromAliasToName(input.getName(), input.getName());
                }
            }
            List<DataDefinitionDeclaration> outputs = step.getStepDefinition().outputs();
            for(DataDefinitionDeclaration output: outputs){
                if (!thisOutputExistInTheMapOfOutputs(step ,output.getName())){
                    step.addToMapOfOutput(output.getName(), output.getName());
                    step.addToMapOfOutputFromAliasToName(output.getName(), output.getName());
                }
            }
        }
    }
    public boolean thisInputExistInTheMapOfInput(StepUsageDeclaration step,String inputName){
        if (step.thisValueExistInTheMapInput(inputName))
            return true;
        else
            return false;
    }
    public boolean thisOutputExistInTheMapOfOutputs(StepUsageDeclaration step,String outputName){
        if (step.thisValueExistInTheMapOutput(outputName))
            return true;
        else
            return false;
    }
    public boolean checkIfNameFoundInInput(StepUsageDeclaration step,String sourceName){
       List<DataDefinitionDeclaration> inputs =  step.getStepDefinition().inputs();
       for (DataDefinitionDeclaration input:inputs){
           if (input.getName().equals(sourceName))
               return true;
       }
       return false;
    }
    public void updateAliasesPerStep() throws FlowDefinitionException {
        for (FlowDefinitionImpl flow : flows) {
            for (FlowLevelAlias alias : flow.getFlowLevelAliases()) {
                StepUsageDeclaration stepToAddTo =flow.getStepByName(alias.getSource());
                if (stepToAddTo ==null)//step not found flow definition exception
                    throw new FlowDefinitionException(FlowDefinitionExceptionItems.STEP_IN_ALIAS_NOT_FOUND,alias.getSource());
                else
                    stepToAddTo.addAlias(alias.getSourceData(),alias.getAlias());
            }
        }
    }

    public void addFlowExecution(FlowExecution flowTestExecution) {
        this.flowExecutions.add(flowTestExecution);
        //maybe logic of time and stuff
    }

    public ExecutionManager getExecutionManager() {
        return executionManager;
    }

    public FlowDefinitionImpl getFlowFromName(String flowName) {
        for (FlowDefinitionImpl flow : flows) {
            if (flow.getName().equals(flowName))
                return flow;
        }
        return null;
    }

    public List<String> getUsers() {
//todo change to real users
        return Arrays.asList("user1", "user2", "user3");
    }
}