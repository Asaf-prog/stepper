package Menu;

import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;
import Menu.MainMenuItems;
import java.util.Scanner;

import static Menu.MainMenuItems.MAIN_MENU;

public class FlowDefinitionMenu implements Menu{

    public static void displayMenu(){
        Stepper stepperData = DataManager.getData();
        System.out.println("---Flow Definition Menu---");
        System.out.println("Choose a flow to get its information:");
        int i=1;
        for(FlowDefinitionImpl flow : stepperData.getFlows()){
            System.out.println("("+i+") "+ flow.getName());
            i++;
        }
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        //TODO: add validation
        if (choice== MAIN_MENU.getValue()){
            return;
        }
        try{
            PresentFlowInformation(stepperData.getFlows().get(choice-1));
        }
        catch (Exception e){
            System.out.println(" Opps,no can do!");
            return;
        }
        return;
    }

    private static void PresentFlowInformation(FlowDefinitionImpl flow) throws Exception {
        System.out.println("Flow name: " + flow.getName());
        System.out.println("Flow description: " + flow.getDescription());
        System.out.println("Steps Formal Outputs: " + flow.getFlowOutputs());
        System.out.println("Is the Step ReadOnly? " + flow.IsReadOnly());
        System.out.println("Get More Information about the Flow: ");
        System.out.println("(1)Steps\n(2)Free Inputs\n(3)Outputs\n(4)Done");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        //todo validate input
        switch(choice){
            case 0://main menu
                return;
            case 1:
                PresentStepDefInfo(flow);
                break;
            case 2:
                PresentFreeInputsInfo(flow);
                break;
            case 3:
                PresentOutputsInfo(flow);
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input");
                break;
        }
        return;
    }

    private static void PresentOutputsInfo(FlowDefinitionImpl flow) {
        int i = 1;
        for (String data : flow.getFlowOfAllStepsOutputs()) {
            System.out.println("(" + i++ + ")" + data);
            //todo add step connected to ,who create it ,and type!

        }
        System.out.println("Press 'Enter' to continue");
        Scanner input = new Scanner(System.in);
        input.nextLine();

    }

    private static void PresentFreeInputsInfo(FlowDefinitionImpl flow) {
        System.out.println("Choose Free Input to display its information:");
        Scanner input = new Scanner(System.in);
        int choice = 69;
        while (choice != MAIN_MENU.getValue()) {
            int i = 1;
            for (Pair<String, DataDefinitionDeclaration> data : flow.getFreeInputs()) {
                System.out.println("(" + i + ")" + data.getKey() +" of the type"+ data.getValue().dataDefinition().getType());
                System.out.println("is Mandatory? " + data.getValue().isMandatory());
                i++;
            }
            choice = input.nextInt();
            //todo add validation
            System.out.println("Press 'Enter' to continue");
            input.nextLine();
            input.nextLine();

        }
    }

    private static void PresentStepDefInfo(FlowDefinitionImpl flow) {
        System.out.println("Choose Step to display its information:");
        Scanner input = new Scanner(System.in);
        int choice=69;

        while(choice!= MAIN_MENU.getValue()){
            int i=1;
            for(StepUsageDeclaration step : flow.getSteps()){
                System.out.println("("+i+")"+ step.getFinalStepName());
                i++;
            }
            choice = input.nextInt();
            PrintStepInformation(flow.getSteps().get(choice-1));
            System.out.println("Press 'Enter' to continue");
            input.nextLine();
            input.nextLine();

            //TODO: add validation
            System.out.println("Choose Step to display its information:");

        }
    }

    private static void PrintStepInformation(StepUsageDeclaration step) {
        System.out.println("Step name: " + step.getFinalStepName());
        System.out.println("Step original name: " + step.getStepDefinition().getName());
        System.out.println("Step ReadOnly? : " + step.getStepDefinition().isReadonly());
      //  System.out.println("Step Skip if Fail? : "+step.skipIfFail());

    }

    @Override
    public void displayMenu2() {
    }
}
