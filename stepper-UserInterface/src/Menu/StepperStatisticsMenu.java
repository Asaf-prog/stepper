package Menu;


import modules.DataManeger.DataManager;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.stepper.Stepper;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StepperStatisticsMenu {

    public static void displayMenu() {
        Stepper stepperData = DataManager.getData();
        System.out.println("---Stepper Statistics Menu---");
        if (stepperData.getFlows()!=null) {
            System.out.println("Stepper currently has " + stepperData.getFlows().size() + " flows");
            System.out.println("Choose Flow to display its statistics:");
            System.out.println("(0) Back");
            for (int i = 0; i < stepperData.getFlows().size(); i++) {
                System.out.println("("+(i+1) +") " + stepperData.getFlows().get(i).getName());
            }
            Scanner input = new Scanner(System.in);
            try {
            int choice = input.nextInt();
            if (choice == MainMenuItems.MAIN_MENU.getValue()) {
                return;

            }
            if (choice > stepperData.getFlows().size() || choice < 1) {
                System.out.println("No such option , try again");
                displayMenu();
            }
            presentFlowStats(stepperData.getFlows().get(choice - 1));
            } catch (Exception e) {
                if (e instanceof InputMismatchException) {
                    System.out.println("Insert number please...");
                    displayMenu();
                }if (e instanceof MenuException) {
                    System.out.println(e.getMessage());;
                } else {
                    return;
                }
                return;
            }
        } else {
            System.out.println("Stepper currently has no flows");
            Scanner input = new Scanner(System.in);
            System.out.println("Press enter to continue");
            input.nextLine();
            displayMenu();

        }
        return;



    }

    private static void presentFlowStats(FlowDefinitionImpl flowDefinition) throws MenuException {
        try {
            System.out.println("Flow name: " + flowDefinition.getName());
            System.out.println("Used " + flowDefinition.getTimesUsed() + " times");
            System.out.println("Took in avg " + flowDefinition.getAvgTime() + " MS");
            presentStepsStats(flowDefinition);
        } catch (Exception e) {
            if (e instanceof InputMismatchException) {
                System.out.println("Insert number please...");
                presentFlowStats(flowDefinition);
            }else {
                System.out.println(e.getMessage());
                throw new MenuException(MenuExceptionItems.EMPTY, e.getMessage() + " from present Flow Stats");
            }
        }
    }
    private static void presentStepsStats(FlowDefinitionImpl flowDefinition) throws MenuException {

        List<StepUsageDeclaration> steps=flowDefinition.getSteps();
        Scanner input = new Scanner(System.in);
        int choice=69;
        //TODO: add validation
        while(choice!=MainMenuItems.MAIN_MENU.getValue()){
            presentFlowSteps(steps);
            choice = input.nextInt();
            if (choice != MainMenuItems.MAIN_MENU.getValue())
                presentStepStats(steps.get(choice - 1));
        }
        return;
    }

    private static void presentFlowSteps(List<StepUsageDeclaration> steps) {
        System.out.println("Choose Step to display its statistics:");
        System.out.println("(0)Back");
        for (int i = 0; i < steps.size(); i++) {
            System.out.println("("+(i+1 )+") " + steps.get(i).getFinalStepName());
        }
    }

    private static void presentStepStats(StepUsageDeclaration step) {
        System.out.println("Step name : " + step.getFinalStepName());
        System.out.println("Used  " + step.getTimeUsed() + " times");
        System.out.println("Average time : " + step.getAvgTime()+ " MS");
        System.out.println("Press Enter to continue");
        Scanner input = new Scanner(System.in);
        input.nextLine();
    }

}
