package modules.DataManeger;

import Menu.MenuException;
import Menu.MenuExceptionItems;

import modules.stepper.Stepper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class DataManager {
    public static Stepper stepperData;//the one and only steppe!!!


    public DataManager(Stepper stepperData){
        this.stepperData = stepperData;
    }
    public DataManager(){
        stepperData = new Stepper();
    }

    public static void setData(Stepper stepperData) {
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


    public static boolean saveData() throws MenuException {
//        String filename = "/Users/cohen/Documents/GitHub/stepper/data/stepperData";
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the path to the data file");
        String filePath = input.nextLine();
        Optional<Path> path= Optional.ofNullable(Paths.get(filePath));
        if (!path.isPresent()) {
            throw new MenuException(MenuExceptionItems.EMPTY, " Invalid Path... back to main menu");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(stepperData);
            return true;
        } catch (IOException e) {
            throw new MenuException(MenuExceptionItems.EMPTY, " Stepper Data File wont created...(try enter good path to your file)");
        }
        catch (Exception e){
            throw new MenuException(MenuExceptionItems.EMPTY, " unknown error..");
        }

    }
    public static boolean saveDataGui(String filePath) throws MenuException {
//        String filename = "/Users/cohen/Documents/GitHub/stepper/data/stepperData";
        Optional<Path> path= Optional.ofNullable(Paths.get(filePath));
        if (!path.isPresent()) {
            throw new MenuException(MenuExceptionItems.EMPTY, " Invalid Path... back to main menu");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(stepperData);
            return true;
        } catch (IOException e) {
            throw new MenuException(MenuExceptionItems.EMPTY, " Stepper Data File wont created...(try enter good path to your file)");
        }
        catch (Exception e){
            throw new MenuException(MenuExceptionItems.EMPTY, " unknown error..");
        }

    }
    public static boolean isFilePathExist(String path) {
        File file = new File(path);
        return file.isFile() && file.exists();
    }
    public static boolean loadData() throws MenuException {
//        String filename = "/Users/cohen/Documents/GitHub/stepper/data/stepperData";
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the path to the data file");
        String filePath = input.nextLine();
        if (!isFilePathExist(filePath)) {
            throw new MenuException(MenuExceptionItems.EMPTY, " Stepper Data File Dont Exist...");
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            stepperData = (Stepper) in.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("BASA9");
        }
        return false;
    }
    public static boolean loadDataGui(String filePath) throws MenuException {
//        String filename = "/Users/cohen/Documents/GitHub/stepper/data/stepperData";
        if (!isFilePathExist(filePath)) {
            throw new MenuException(MenuExceptionItems.EMPTY, " Stepper Data File Dont Exist...");
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

}
