package modules.DataManeger;

import modules.stepper.Manager;
import modules.stepper.Stepper;

public class DataManager implements Manager {
    static Stepper stepperData;//the one and only steppe!!!

    public DataManager(Stepper stepperData){
        this.stepperData = stepperData;
    }
    public DataManager(){
        stepperData = new Stepper();
    }

    public void setStepperData(Stepper stepperData) {
        this.stepperData = stepperData;
    }

    public Stepper getStepperData() {
        return stepperData;
    }

    public static Stepper getData(){
        return stepperData;
    }



}