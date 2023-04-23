package modules.DataManeger;

import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;
import schemeTest.generatepackage.STFlow;
import schemeTest.generatepackage.STStepper;

import java.util.List;

public class DeepCopy {

private STStepper stStepper;
private Stepper stepper;
private List <STFlow> Flows;

    public DeepCopy(STStepper stStepper){
        this.stStepper = stStepper;
        Flows = stStepper.getSTFlows().getSTFlow();
        stepper = new Stepper();
    }

    public void setStStepper(STStepper stStepper) {this.stStepper = stStepper;}
    public STStepper getStStepper() {return stStepper;}
    public Stepper getStepper() {return stepper;}

    public Stepper copyAllDataInFields(){
        //Stepper res = new Stepper();
    for (STFlow flow : Flows){
    stepper.copyFlow(flow);
    }
    return null;
    }

    void copySpecificFlow(FlowDefinitionImpl flow){
    }
}
