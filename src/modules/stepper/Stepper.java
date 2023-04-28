package modules.stepper;
 import modules.Map.CustomMapping;
 import modules.Map.FlowLevelAlias;
 import modules.flow.definition.api.FlowDefinitionImpl;
 import modules.flow.definition.api.StepUsageDeclaration;
 import modules.flow.definition.api.StepUsageDeclarationImpl;
 import modules.flow.execution.FlowExecution;
 import modules.step.StepDefinitionRegistry;
 import modules.step.api.DataDefinitionDeclaration;
 import schemeTest.generatepackage.STCustomMapping;
 import schemeTest.generatepackage.STFlow;
 import schemeTest.generatepackage.STFlowLevelAlias;
 import schemeTest.generatepackage.STStepInFlow;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.Optional;

public class Stepper implements Manager {
    public static int idCounter = 1000;
    List<FlowExecution> flowExecutions;
    List<FlowDefinitionImpl> flows;



    public List<FlowExecution> getFlowExecutions() {
        return flowExecutions;
    }

    public void setFlowExecutions(List<FlowExecution> flowExecutions) {
        this.flowExecutions = flowExecutions;
    }
    public FlowExecution getFlowExecutionById(int id){
        Optional<FlowExecution> res = flowExecutions.stream().filter(flowExecution -> flowExecution.getUniqueId() == id).findFirst();
        if(res.isPresent()){
            return res.get();
        }
        return null;
    }
    public static int GetUniqueID(){
        return ++idCounter;
    }

    public Stepper(){
        flows = new ArrayList<>();
        flowExecutions = new ArrayList<>();
    }

    public void setFlows(List<FlowDefinitionImpl> flows) {
        this.flows = flows;
    }

    public List<FlowDefinitionImpl> getFlows() {
        return flows;
    }
    public void copyFlowFromXMLObject(STFlow stFlow) {
        FlowDefinitionImpl flowToAdd = new FlowDefinitionImpl(stFlow.getName(), stFlow.getSTFlowDescription());
        List<FlowLevelAlias> flowLevelAliasesToAdd = new ArrayList<>();
        //add all steps
        for (int i = 0; i < stFlow.getSTStepsInFlow().getSTStepInFlow().size(); i++) {
            //todo check if it's costume mapping or automatic mapping

            STStepInFlow currStStep=stFlow.getSTStepsInFlow().getSTStepInFlow().get(i);
            String StepName = currStStep.getName();
            String StepNameAlias = currStStep.getAlias();
            StepUsageDeclarationImpl declaration =new StepUsageDeclarationImpl(StepDefinitionRegistry.getStepDefinitionByName(StepName));

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
        if (stFlow.getSTCustomMappings()!=null) { //this step is costume mapping
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
        if (stFlow.getSTFlowLevelAliasing()!=null) {
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
        flowToAdd.setFlowLevelAliases(flowLevelAliasesToAdd);
        flows.add(flowToAdd);
    }
    public void validateStepper() {
        //this.updateAliasesPerStep();
        for (FlowDefinitionImpl flow : flows) {

            //todo -saar check if the xml file exist
            //todo -saar=> check if for every flow unique name

            //flow.setFinalNames();//finish flow definition
            inisilaizedInputAndOutput(flow.getSteps(),flow.getFlowLevelAliases());

           // printfunction(flow.getSteps());

            flow.createFlowFreeInputs();//including one for user input
            flow.setReadOnlyState();
            flow.validateFlowStructure();
        }
    }
    public void printfunction(List<StepUsageDeclaration> stepOfFlow){
        for (StepUsageDeclaration step: stepOfFlow){
            System.out.println("the step is "+step.getFinalStepName());
            System.out.println("*****************");
            List<DataDefinitionDeclaration> in = step.getStepDefinition().inputs();
            for (DataDefinitionDeclaration i : in){
                System.out.println(step.getByKeyFromInputMap(i.getName()));
            }
            List<DataDefinitionDeclaration> out = step.getStepDefinition().outputs();

            System.out.println("outputttttttttttt");
            System.out.println("*****************");

            for (DataDefinitionDeclaration o : out){
                System.out.println(step.getByKeyFromOutputMap(o.getName()));
            }
        }
    }
    public void inisilaizedInputAndOutput(List<StepUsageDeclaration> stepOfFlow,List<FlowLevelAlias> flowLevelAliases){
        for (StepUsageDeclaration step: stepOfFlow){
            for (FlowLevelAlias alias: flowLevelAliases){
                if (step.getFinalStepName().equals(alias.getSource())){
                    if (checkIfNameFoundInInput(step,alias.getSourceData())){
                        step.addToMapOfInput(alias.getSourceData(), alias.getAlias());
                    }
                    else //is belonging to output map
                        step.addToMapOfOutput(alias.getSourceData(), alias.getAlias());
                }
            }
        }
        for (StepUsageDeclaration step: stepOfFlow){
            List<DataDefinitionDeclaration> inputs =  step.getStepDefinition().inputs();
            for (DataDefinitionDeclaration input:inputs){
                if (!thisInputExistInTheMapOfInput(step,input.getName())){
                    step.addToMapOfInput(input.getName(), input.getName());
                }
            }
            List<DataDefinitionDeclaration> outputs = step.getStepDefinition().outputs();
            for(DataDefinitionDeclaration output: outputs){
                if (!thisOutputExistInTheMapOfOutputs(step ,output.getName())){
                    step.addToMapOfOutput(output.getName(), output.getName());
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
    public void updateAliasesPerStep() {
        for (FlowDefinitionImpl flow : flows) {
            for (FlowLevelAlias alias : flow.getFlowLevelAliases()) {
                StepUsageDeclaration stepToAdd=flow.getStepByName(alias.getSource());
                stepToAdd.addAlias(alias.getSourceData(),alias.getAlias());
            }
        }
    }

    public void AddFlowExecution(FlowExecution flowTestExecution) {
        this.flowExecutions.add(flowTestExecution);
        //maybe logic of time and stuff
    }
}