package Menu;

import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;
import java.util.InputMismatchException;
import java.util.Scanner;

import static Menu.MainMenuItems.MAIN_MENU;

public class FlowDefinitionMenu{

    public static void displayMenu() throws MenuException {
        Stepper stepperData = DataManager.getData();
        if (stepperData.getFlows().isEmpty()) {
            throw new MenuException(MenuExceptionItems.EMPTY, " No Data Loaded");
        }
        System.out.println("---Flow Definition Menu---");
        while(true) {
            System.out.println("Choose a flow to get its information:");
            int i = 1;
            for (FlowDefinitionImpl flow : stepperData.getFlows()) {
                System.out.println("(" + i + ") " + flow.getName());
                i++;
            }
            Scanner input = new Scanner(System.in);

            try {
                int choice = input.nextInt();
                if (choice == 0)
                    return;
                if (choice > stepperData.getFlows().size() || choice < 1) {
                    System.out.println("No such option , try again");
                    continue;
                }
                PresentFlowInformation(stepperData.getFlows().get(choice - 1));
                continue;
            }catch (InputMismatchException e){
                System.out.println("Invalid input, please try again");
                input.next();
                continue;
            } catch (Exception e) {
                throw new MenuException(MenuExceptionItems.EMPTY, "Error in presenting flow information");
            }

        }

    }
    private static void GetUserInput(Stepper stepperData) throws MenuException {
        Scanner input = new Scanner(System.in);
        while(true) {
            try {
                int choice = input.nextInt();
                if (choice == 0)
                    return;
                if (choice > stepperData.getFlows().size() || choice < 1) {
                    System.out.println("No such option , try again");
                    continue;
                }
                PresentFlowInformation(stepperData.getFlows().get(choice - 1));
                continue;
            }catch (InputMismatchException e){
                System.out.println("Invalid input, please try again");
                input.next();
                continue;
            } catch (Exception e) {
                    throw new MenuException(MenuExceptionItems.EMPTY, "Error in presenting flow information");
            }

        }
    }

    private static void PresentFlowInformation(FlowDefinitionImpl flow) throws MenuException {
        while(true) {
            System.out.println("Flow name: " + flow.getName());
            System.out.println("Flow description: " + flow.getDescription());
            System.out.println("Steps Formal Outputs: " + flow.getFlowOutputs());
            System.out.println("Is the Step ReadOnly? " + flow.IsReadOnly());
            System.out.println("Get More Information about the Flow: ");
            System.out.println("(1) Steps\n(2) Free Inputs\n(3) Outputs");
            Scanner input = new Scanner(System.in);

            try {
                int choice = input.nextInt();
                switch (choice) {
                    case 0://main menu
                        return;
                    case 1:
                        PresentStepDefInfo(flow);
                        continue;
                    case 2:
                        PresentFreeInputsInfo(flow);
                        continue;
                    case 3:
                        PresentOutputsInfo(flow);
                        continue;
                    default:
                        System.out.println("No such option , try again");
                        continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please try again");
                input.next();
                continue;
            } catch (Exception e) {
                throw new MenuException(MenuExceptionItems.EMPTY, "Error in presenting flow information");
            }
        }
    }
    private static void PresentOutputsInfo(FlowDefinitionImpl flow) {
        int i = 1;
        for (String data : flow.getFlowOfAllStepsOutputs()) {
            System.out.println("(" + i++ + ") " + data);

        }
        System.out.println("Press 'Enter' to continue");
        Scanner input = new Scanner(System.in);
        input.nextLine();

    }

    private static void PresentFreeInputsInfo(FlowDefinitionImpl flow) throws MenuException {
        System.out.println("Choose Free Input to display its information: (Or 0 to return)");
        Scanner input = new Scanner(System.in);
        int choice = 69;
        while (choice != MAIN_MENU.getValue()) {
            int i = 1;
            for (Pair<String, DataDefinitionDeclaration> data : flow.getFreeInputs()) {
                System.out.println("(" + i + ") " + data.getKey() +" of the type "+ data.getValue().dataDefinition().getTypeName());
                System.out.println("is Mandatory? " + data.getValue().isMandatory());
                i++;
            }
            while(true) {
                try {
                    choice = input.nextInt();
                    if (choice == 0)
                        return;
                    if (choice > flow.getFreeInputs().size() || choice < 1) {
                        System.out.println("No such option , try again");
                        continue;
                    }
                }catch (InputMismatchException e) {
                    System.out.println("Invalid input, please try again");
                    input.next();
                    continue;
                }
                break;
            }
            System.out.println("Free input original name: " + flow.getFreeInputs().get(choice - 1).getKey() +
                    "\nUser String: " + flow.getFreeInputs().get(choice - 1).getValue().getUserString());
            System.out.println("Press 'Enter' to continue");
            input.nextLine();
            input.nextLine();
        }
    }
    private static void PresentStepDefInfo(FlowDefinitionImpl flow) throws MenuException {
        Scanner input = new Scanner(System.in);
        int choice=69;
        try {
            while (choice != MAIN_MENU.getValue()) {
                int i = 1;
                System.out.println("Choose Step to display its information:");
                for (StepUsageDeclaration step : flow.getSteps()) {
                    System.out.println("(" + i + ") " + step.getFinalStepName());
                    i++;
                }while(true) {
                    try {
                        choice = input.nextInt();
                        if (choice == 0)
                            return;
                        if (choice > flow.getSteps().size() || choice < 1) {
                            System.out.println("No such option , try again");
                            continue;
                        }
                    }catch (InputMismatchException e) {
                        System.out.println("Invalid input, please try again");
                        input.next();
                        continue;
                    }
                    break;
                }
                PrintStepInformation(flow.getSteps().get(choice - 1));
                System.out.println("Press 'Enter' to continue");
                input.nextLine();
                input.nextLine();


            }
        } catch (Exception e){
            if (e instanceof InputMismatchException) {
                throw new MenuException(MenuExceptionItems.INVALID_NUMBER_INPUT, " Present step Information");
            } else
                throw new MenuException(MenuExceptionItems.EMPTY, "Error in presenting step information");
        }

    }

    private static void PrintStepInformation(StepUsageDeclaration step) {
        System.out.print("Step name: " + step.getFinalStepName());
        System.out.print(" , Step original name: " + step.getStepDefinition().getName());
        System.out.println(" , Step ReadOnly? : " + step.getStepDefinition().isReadonly());
      //  System.out.println("Step Skip if Fail? : "+step.skipIfFail());

    }

}
