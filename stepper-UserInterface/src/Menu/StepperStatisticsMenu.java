package Menu;


import modules.DataManeger.DataManager;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.stepper.Stepper;

import java.util.List;
import java.util.Scanner;

public class StepperStatisticsMenu implements Menu {

    public static void displayMenu() {
        Stepper stepperData = DataManager.getData();
        System.out.println("---Stepper Statistics Menu---");
        if (stepperData.getFlows()!=null) {
            System.out.println("Stepper currently has " + stepperData.getFlows().size() + " flows");
            System.out.println("Choose Flow to display its statistics:");
            System.out.println("(0)Back");
            for (int i = 0; i < stepperData.getFlows().size(); i++) {
                System.out.println("("+(i+1) +")" + stepperData.getFlows().get(i).getName());
            }
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();


            //TODO: add validation
            if (choice == MainMenuItems.MAIN_MENU.getValue()) {
                return;
            }
            try {
                presentFlowStats(stepperData.getFlows().get(choice - 1));
            } catch (Exception e) {
                System.out.println("im so sowwy ,no can do!");
                return;
            }
        }
        else
            System.out.println("Stepper currently has no flows");


    }

    private static void presentFlowStats(FlowDefinitionImpl flowDefinition) {
        System.out.println("Flow name: " + flowDefinition.getName());
        System.out.println("Used " + flowDefinition.getTimesUsed() + " times");
        System.out.println("Took in avg " + flowDefinition.getAvgTime()+ " MS");
        presentStepsStats(flowDefinition.getSteps());
    }
    private static void presentStepsStats(List<StepUsageDeclaration> steps) {
        Scanner input = new Scanner(System.in);
        int choice;
        //TODO: add validation
        do {
            presentFlowSteps(steps);
            choice = input.nextInt();
            if (choice != MainMenuItems.MAIN_MENU.getValue())
                presentStepStats(steps.get(choice - 1));
        }while(choice!=MainMenuItems.MAIN_MENU.getValue());
        System.out.println("Back to main menu...");
    }

    private static void presentFlowSteps(List<StepUsageDeclaration> steps) {
        System.out.println("Choose Step to display its statistics:");
        System.out.println("(0)Back");
        for (int i = 0; i < steps.size(); i++) {
            System.out.println("("+(i+1 )+")" + steps.get(i).getFinalStepName());
        }
    }

    private static void presentStepStats(StepUsageDeclaration step) {
        System.out.println("Step name: " + step.getFinalStepName());
        System.out.println("Used  " + step.getTimeUsed() + " times");
        System.out.println("Took in avg  " + step.getAvgTime()+ " MS");
        System.out.println("Press Enter to continue");
        Scanner input = new Scanner(System.in);
        input.nextLine();
    }

    @Override
    public void displayMenu2() {

    }
}
