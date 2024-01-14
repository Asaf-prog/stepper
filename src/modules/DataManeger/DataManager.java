package modules.DataManeger;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DataManager {
    public static Stepper stepperData;//the one and only steppe!!!
    //UserManager userManager;
    RoleManager roleManager;

    public DataManager(Stepper stepperData) {
        this.stepperData = stepperData;
        //this.userManager = new UserManager();
        roleManager = new RoleManager(stepperData.getFlows());

    }
    public DataManager() {
        stepperData = new Stepper();
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public static void setData(Stepper stepperData) {
    }

    public void setStepperData(Stepper stepperData) {
        this.stepperData = stepperData;
    }

    public Stepper getStepperData() {
        return stepperData;
    }

    public static Stepper getData() {
        return stepperData;
    }


    public static boolean saveData() throws Exception {
//        String filename = "/Users/cohen/Documents/GitHub/stepper/data/stepperData";
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the path to the data file");
        String filePath = input.nextLine();
        Optional<Path> path = Optional.ofNullable(Paths.get(filePath));
        if (!path.isPresent()) {
//            throw new MenuException(MenuExceptionItems.EMPTY, " Invalid Path... back to main menu");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(stepperData);
            return true;
        } catch (IOException e) {
//            throw new MenuException(MenuExceptionItems.EMPTY, " Stepper Data File wont created...(try enter good path to your file)");
        } catch (Exception e) {
//            throw new MenuException(MenuExceptionItems.EMPTY, " unknown error..");
        }
        return false;
    }

    public static boolean saveDataGui(String filePath) throws Exception {
        return false;
    }

    public static boolean isFilePathExist(String path) {
        File file = new File(path);
        return file.isFile() && file.exists();
    }

    public static boolean loadData() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the path to the data file");
        String filePath = input.nextLine();
        if (!isFilePathExist(filePath)) {
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            stepperData = (Stepper) in.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("BASA9");
        }
        return false;
    }

    public static boolean loadDataGui(String filePath) throws Exception {
        if (!isFilePathExist(filePath)) {
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            stepperData = (Stepper) in.readObject();

            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading data from file: " + filePath);
            // e.printStackTrace();
        }
        return false;
    }

    public List<FlowDefinitionImpl> getFlowsForRole(List<String> roles) {
        List<FlowDefinitionImpl> flows = new ArrayList<>();
        for (String role : roles) {
            Role r1 = roleManager.getRoleByName(role);
            flows.addAll(r1.getFlows());
        }
        return flows;
    }


    public void updateStepper(Stepper toAdd)throws Exception {

        try {
            stepperData.setNewStepper(toAdd);
            stepperData.validateStepperContinuation();
        } catch (Exception e){
            throw new Exception("Stepper is not valid"+e.getMessage());
        }
    }

    public void updateRoles() {
        roleManager.updateRoles(stepperData.getFlows());
    }
}
