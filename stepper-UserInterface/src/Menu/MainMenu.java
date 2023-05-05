package Menu;

import modules.DataManeger.DataManager;
import modules.DataManeger.GetDataFromXML;
import modules.stepper.Stepper;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu{

    public static void main(String[] args) {
        System.out.println("---Stepper UI---");
       // GetDataFromXML.fromXmlFileToObject("/Users/cohen/Documents/GitHub/stepper/ex1.xml");
       // Stepper stepperData= DataManager.getData();
        Scanner input = new Scanner(System.in);
       MainMenuItems choice = null;
        while (choice != MainMenuItems.EXIT){
            displayMenu();
            try {
                choice =MainMenuItems.getMenuItem(input.nextInt());
                if(choice==null)
                    throw new MenuException(MenuExceptionItems.INVALID_NUMBER_INPUT, "from Main menu Options");
                if ((choice.getValue() < 0) || (choice.getValue() > MainMenuItems.values().length) || (choice == MainMenuItems.MAIN_MENU)) {
                    throw new MenuException(MenuExceptionItems.INVALID_NUMBER_INPUT, "from Main menu Options");
                }
                switch (choice) {
                    //todo add logics to choices
                    case MAIN_MENU:
                        break;
                    case DATA_MANAGEMENT:
                        LoadDataMenu.displayMenu();
                        break;
                    case FLOW_DEFINITION_MENU:
                        FlowDefinitionMenu.displayMenu();
                        break;
                    case FLOW_EXECUTION:
                        FlowExecutionMenu.displayMenu();
                        break;
                    case FLOW_STATISTICS:
                        FlowStatisticsMenu.displayMenu();
                        break;
                    case STEPPER_STATISTICS:
                        StepperStatisticsMenu.displayMenu();
                        break;
                    case EXIT:
                        System.out.println("Exiting... Goodbye!");
                        break;
                }
            } catch (Exception e) {
                if (e instanceof MenuException) {
                    System.out.println(e.getMessage());
                    System.out.println("Please try again");
                }else if(e instanceof InputMismatchException) {
                    System.out.println("Invalid input, please try again");
                    input.nextLine();
                    choice=null;
                } else {
                    System.out.println("Possible Missing Data in the stepper, please load data again...");
                    System.out.println("Please try again");
                }
            }
        }
        input.close();
    }
    public static void displayMenu() {
        System.out.println("---Main Menu---");
        for (MainMenuItems item : MainMenuItems.values()) {
            if (item == MainMenuItems.MAIN_MENU) {//no need to present main menu option in the Main Menu
                continue;
            }
            System.out.println("("+item.ordinal() + ") " + item.getName());
        }
    }
}

