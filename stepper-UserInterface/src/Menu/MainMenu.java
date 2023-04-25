package Menu;

import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.stepper.Stepper;
import java.util.Scanner;

public class MainMenu implements Menu {

    public static void main(String[] args) {
        System.out.println("---Stepper UI---");

        Scanner input = new Scanner(System.in);
        MainMenuItems choice = null;
        do {
            displayMenu();
            try {
                choice = MainMenuItems.getMenuItem(input.nextInt());
                if ((choice.getValue() < 0) || (choice.getValue() > MainMenuItems.values().length) || (choice == MainMenuItems.MAIN_MENU)) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please try again");
                input.nextLine();
                continue;
            }
                switch (choice) {
                    //todo add logics to choices
                    case MAIN_MENU:
                        System.out.println("MAIN_MENU");
                        break;
                    case LOAD_DATA:
                        System.out.println("LOAD_DATA");
                        GetXmlDataFromUser();
                        break;
                    case FLOW_DEFINITION_MENU:
                        System.out.println("FLOW_DEFINITION_MENU");
                        FlowDefinitionMenu.displayMenu();
                        break;
                    case FLOW_EXECUTION:
                        System.out.println("FLOW_EXECUTION");
                        FlowExecutionMenu.displayMenu();
                        break;
                    case FLOW_STATISTICS:
                        System.out.println("FLOW_STATISTICS");
                        break;
                    case STEPPER_STATISTICS:
                        System.out.println("STEPPER_STATISTICS");
                        break;
                    case EXIT:
                        System.out.println("Exiting... Goodbye!");
                        break;
                }
            } while (choice != MainMenuItems.EXIT);
        input.close();
    }
    private static void GetXmlDataFromUser() {
        System.out.println("Please enter the path to the xml file");
        Scanner input = new Scanner(System.in);
        String path = input.nextLine();

        //todo add logics to get the xml file
        GetDataFromXML.fromXmlFileToObject(path);

        Stepper stepperData= DataManager.getData();

    }
    public static void displayMenu() {
        System.out.println("MainMenu:");
        for (MainMenuItems item : MainMenuItems.values()) {
            if (item == MainMenuItems.MAIN_MENU) {//no need to present main menu option in the Main Menu
                continue;
            }
            System.out.println(item.ordinal() + " - " + item.getName());
        }
    }
    @Override
    public void displayMenu2() {

    }
}
