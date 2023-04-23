package modules.stepper;
 import modules.flow.definition.api.FlowDefinitionImpl;
 import modules.flow.definition.api.StepUsageDeclarationImpl;
 import modules.step.StepDefinitionRegistry;
 import schemeTest.generatepackage.STFlow;

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
    public void copyFlow(STFlow stFlow){
        FlowDefinitionImpl  flowToAdd = new FlowDefinitionImpl(stFlow.getName(),stFlow.getSTFlowDescription());

       for (int i = 0; i< stFlow.getSTStepsInFlow().getSTStepInFlow().size(); i++){
          //todo check if it's costume mapping or automatic mapping
           //if (costume)
           //validate
           //else
           //validate automatic

           String StepName= stFlow.getSTStepsInFlow().getSTStepInFlow().get(i).getName();
           //StepDefinitionRegistry myEnum = myEnum.valueOf(StepName);


           flowToAdd.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.getStepDefinitionByName(StepName)));

         }
        flows.add(flowToAdd);
    }
}
