package modules.DataManeger;

import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.FlowDefinitionException;
import modules.stepper.Stepper;
import schemeTest2.generatepackage.STFlow;
import schemeTest2.generatepackage.STStepper;

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

    public Stepper copyAllDataInFields() throws FlowDefinitionException {
        //Stepper res = new Stepper();
        stepper.setTPSize(stStepper.getSTThreadPool());
        for (STFlow flow : Flows){
        stepper.copyFlowFromXMLObject(flow);
        }
        return stepper;
        }
}
