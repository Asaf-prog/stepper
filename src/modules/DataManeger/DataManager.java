package modules.DataManeger;

import modules.stepper.Manager;
import modules.stepper.Stepper;

import java.io.*;
import java.util.Scanner;

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

    public static boolean saveData() {
        String filename = "/Users/cohen/Documents/GitHub/stepper/data/stepperData";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(stepperData);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean loadData() {
        String filename = "/Users/cohen/Documents/GitHub/stepper/data/stepperData";
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            stepperData = (Stepper) in.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
