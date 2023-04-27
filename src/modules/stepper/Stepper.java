package modules.stepper;
 import modules.Map.CustomMapping;
 import modules.Map.FlowLevelAlias;
 import modules.flow.definition.api.FlowDefinitionImpl;
 import modules.flow.definition.api.StepUsageDeclaration;
 import modules.flow.definition.api.StepUsageDeclarationImpl;
 import modules.flow.execution.FlowExecution;
 import modules.step.StepDefinitionRegistry;
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
        this.updateAliasesPerStep();
        for (FlowDefinitionImpl flow : flows) {
            //todo finish flow definition

            flow.setFinalNames();//finish flow definition
            flow.createFlowFreeInputs();//including one for user input
            flow.setReadOnlyState();
            flow.validateFlowStructure();
        }
    }
    public void updateAliasesPerStep() {
        for (FlowDefinitionImpl flow : flows) {
            for (FlowLevelAlias alias : flow.getFlowLevelAliases()) {
                StepUsageDeclaration stepToAdd=flow.getStepByName(alias.getSource());
                stepToAdd.addAlias(alias.getSourceData(),alias.getAlias());
            }
        }
    }
}
