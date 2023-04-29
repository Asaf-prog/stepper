package Menu;

import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;
import Menu.MainMenuItems;
import java.util.Scanner;

public class LoadDataMenu implements Menu {
    public static void displayMenu(){
        System.out.println("---Load Data Menu---");
        Stepper stepperData = DataManager.getData();
        System.out.println("Choose what to do \n 1.Load Existing Data \n 2.Save The Data \n 3.Load Stepper Xml Definition ");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        try {
            switch (choice) {
                case 0://Main menu
                    return;
                case 1:
                    DataManager.loadData();
                    break;
                case 2:
                    DataManager.saveData();
                    break;
                case 3:
                    getXmlDataFromUser();
                    break;
                default:
                    System.out.println("Invalid input, please try again");
                    return;
            }
        }
        catch (Exception e){
            System.out.println("Opps, no can do!");
            return;
        }
    }
    private static void getXmlDataFromUser() {
        System.out.println("Please enter the path to the xml file");
        Scanner input = new Scanner(System.in);
        String path = input.nextLine();

        //todo add logics to get the xml file
        GetDataFromXML.fromXmlFileToObject(path);

        Stepper stepperData= DataManager.getData();

    }
    @Override
    public void displayMenu2() {

    }
}
