package Menu;

import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.stepper.Stepper;

import java.util.Date;
import java.util.Scanner;

public class FlowStatisticsMenu implements Menu {
    public static void displayMenu() {
        System.out.println("Choose a flow to get its Stats:");
        Stepper stepperData = DataManager.getData();
        int i=1;
        if (stepperData.getFlowExecutions()==null){
            System.out.println("No flow executions to show");
            return;
        }
        else {
            System.out.println("0. Back");
            for (FlowExecution exe : stepperData.getFlowExecutions()) {
                String StartTime=exe.getStartDateTime();
                System.out.println(i + ". " + exe.getFlowDefinition().getName() + "  Occurred on " + StartTime);
                i++;
            }
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            if (choice == MainMenuItems.MAIN_MENU.getValue()) {
                return;
            }
            try {
                PresentFlowStats(stepperData.getFlowExecutionById(stepperData.getFlowExecutions().get(choice - 1).getUniqueId()));
            } catch (Exception e) {
                System.out.println("im so sorry ,no can do!");
                return;
            }
        }
    }
    private static void PresentFlowStats(FlowExecution singleExecution) {
        System.out.println("Flow name: " + singleExecution.getFlowDefinition().getName());
        System.out.println("ID: " + singleExecution.getUniqueId());
        //start to work on 'time'
        System.out.println("Took about " +singleExecution.getTotalTime().toMillis() + " MS");
        System.out.println("And finish with " + singleExecution.getFlowExecutionResult().name());
        System.out.println("The flow Outputs are :" + singleExecution.printOutputs());
    }
    @Override
    public void displayMenu2() {

    }
}
