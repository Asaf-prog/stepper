package modules.stepper;
 import modules.Map.CustomMapping;
 import modules.Map.FlowLevelAlias;
 import modules.flow.definition.api.FlowDefinitionImpl;
 import modules.flow.definition.api.StepUsageDeclarationImpl;
 import modules.step.StepDefinitionRegistry;
 import schemeTest.generatepackage.STCustomMapping;
 import schemeTest.generatepackage.STFlow;
 import schemeTest.generatepackage.STFlowLevelAlias;

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

    public void copyFlowFromXMLObject(STFlow stFlow){
        FlowDefinitionImpl  flowToAdd = new FlowDefinitionImpl(stFlow.getName(),stFlow.getSTFlowDescription());
        //add all steps
       for (int i = 0; i< stFlow.getSTStepsInFlow().getSTStepInFlow().size(); i++){
          //todo check if it's costume mapping or automatic mapping
           String StepName= stFlow.getSTStepsInFlow().getSTStepInFlow().get(i).getName();
           flowToAdd.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.getStepDefinitionByName(StepName)));
       }
        //adding custom mappings for each step
        List<STCustomMapping> stCustomMappings = stFlow.getSTCustomMappings().getSTCustomMapping();
        List<CustomMapping> CustomMappingsToAdd = new ArrayList<>();
        for (int j = 0; j< stCustomMappings.size();j++){
            CustomMapping temp=new CustomMapping(stCustomMappings.get(j).getSourceStep()
                    ,stCustomMappings.get(j).getSourceData(),stCustomMappings.get(j).getTargetStep()
                    ,stCustomMappings.get(j).getTargetData());
            CustomMappingsToAdd.add(j,temp);

        }
        flowToAdd.setCustomMappings(CustomMappingsToAdd);

        //adding FlowLevelAlias for each step
        List<FlowLevelAlias> flowLevelAliasesToAdd = new ArrayList<>();
        List<STFlowLevelAlias> stFlowLevelAliases = stFlow.getSTFlowLevelAliasing().getSTFlowLevelAlias();
        for (int k = 0; k< stFlowLevelAliases.size();k++){
            FlowLevelAlias temp=new FlowLevelAlias(stFlowLevelAliases.get(k).getStep()
                    ,stFlowLevelAliases.get(k).getSourceDataName()
                    ,stFlowLevelAliases.get(k).getAlias());
            flowLevelAliasesToAdd.add(k,temp);
        }
       flows.add(flowToAdd);
    }
}
