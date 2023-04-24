package Menu;

import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;

import java.util.Scanner;

public class FlowExecutionMenu implements Menu {

    public static void displayMenu(){

        System.out.println("Flow Chooser Menu:");
        Stepper stepperData = DataManager.getData();
        int i=1;
        for(FlowDefinitionImpl flow : stepperData.getFlows()){
            System.out.println(i+". "+ flow.getName() + "That does: " + flow.getDescription());
            i++;
        }

        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();




    }
    @Override
    public void displayMenu2() {
        System.out.println("FlowChooserMenu:");
        // Stepper stepperData = DataManager::getData();
        for (MainMenuItems item : MainMenuItems.values()) {
            if (item == MainMenuItems.MAIN_MENU) {//no need to present main menu option in the Main Menu
                continue;
            }
            System.out.println(item.ordinal() + " - " + item.getName());
        }
    }
}
