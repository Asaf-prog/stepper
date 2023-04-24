package modules.stepper;
 import modules.Map.CustomMapping;
 import modules.Map.FlowLevelAlias;
 import modules.flow.definition.api.FlowDefinitionImpl;
 import modules.flow.definition.api.StepUsageDeclarationImpl;
 import modules.step.StepDefinitionRegistry;
 import schemeTest.generatepackage.STCustomMapping;
 import schemeTest.generatepackage.STFlow;
 import schemeTest.generatepackage.STFlowLevelAlias;
 import schemeTest.generatepackage.STStepInFlow;

 import java.util.ArrayList;
 import java.util.List;

public class Stepper implements Manager {
    List<FlowDefinitionImpl> flows;
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
        //add all steps
        for (int i = 0; i < stFlow.getSTStepsInFlow().getSTStepInFlow().size(); i++) {
            //todo check if it's costume mapping or automatic mapping
            STStepInFlow currStStep=stFlow.getSTStepsInFlow().getSTStepInFlow().get(i);
            String StepName = currStStep.getName();
            String StepNameAlias = currStStep.getAlias();
            StepUsageDeclarationImpl declaration =new StepUsageDeclarationImpl(StepDefinitionRegistry.getStepDefinitionByName(StepName));
            if (StepNameAlias!=null)
                declaration.setStepNameAlias(StepNameAlias);//if step have alias set it if not set the step name as the alias
            else
                declaration.setStepNameAlias(StepName);

            declaration.setSkipIfFail(stFlow.getSTStepsInFlow().getSTStepInFlow().get(i).isContinueIfFailing());
            flowToAdd.getFlowSteps().add(declaration);

        }
        //adding custom mappings for each step
        if (stFlow.getSTCustomMappings()!=null) {
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
            List<FlowLevelAlias> flowLevelAliasesToAdd = new ArrayList<>();
            List<STFlowLevelAlias> stFlowLevelAliases = stFlow.getSTFlowLevelAliasing().getSTFlowLevelAlias();
            for (int k = 0; k < stFlowLevelAliases.size(); k++) {
                STFlowLevelAlias currStFlowLevelAlias = stFlowLevelAliases.get(k);
                FlowLevelAlias temp = new FlowLevelAlias(currStFlowLevelAlias.getStep()
                        , currStFlowLevelAlias.getSourceDataName()
                        , currStFlowLevelAlias.getAlias());
                flowLevelAliasesToAdd.add(k, temp);
            }
        }
        flows.add(flowToAdd);
    }
}
