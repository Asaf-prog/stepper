package Menu;

import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;
import modules.step.StepDefinitionRegistry;
import modules.step.api.DataDefinitionDeclaration;
import modules.step.api.DataDefinitionDeclarationImpl;
import modules.stepper.Stepper;

import java.util.Scanner;

public class FlowExecutionMenu implements Menu {

    public static void displayMenu(){

        System.out.println("Flow Chooser Menu:");
        Stepper stepperData = DataManager.getData();
        int i=1;
        for(FlowDefinitionImpl flow : stepperData.getFlows()){
            System.out.println(i+". "+ flow.getName() + " That does: " + flow.getDescription());
            //todo => check in the list of the free input(found in the function validateFlowStructure ) the value after aliasing and compare with the real data
            // maybe i will new to hold more Map of data definition <aliasing name,real name>

            i++;
        }
        Scanner input = new Scanner(System.in);
        //todo if input 0 to main menu
        int choice = input.nextInt();
        if (choice== MainMenuItems.MAIN_MENU.getValue()){
            return;
        }try{
            FlowDefinition flow =stepperData.getFlows().get(choice-1);
            getUserInput(flow);
            ExecuteFlow(stepperData,choice);
        }
        catch (Exception e){
            //todo Class Exception for ExecutionException
            System.out.println(" Opps,no can do!");
            return;
        }
    }

    private static void getUserInput(FlowDefinition flow) {

        System.out.println("for Flow :"+ flow.getName()+"Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs " );
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        switch (choice){
            case 1:
                System.out.println("Choose one to insert:");
                for(Pair<String,DataDefinitionDeclaration> pairOfStringAndDD : flow.getFlowFreeInputs()){
                    }
                }

                //free inputs print all mandatory

                break;
            case 2:
           //free inputs print all optional
                break;
            default:
                System.out.println("Wrong input");
                break;
        }
    }

    private static void ExecuteFlow(Stepper stepperData,int choice) {

        System.out.println("Executing flow: "+ stepperData.getFlows().get(choice-1).getName());

        FlowDefinitionImpl flow= stepperData.getFlows().get(choice-1);
       flow.validateFlowStructure();
        FLowExecutor fLowExecutor = new FLowExecutor();

        FlowExecution flowTestExecution = new FlowExecution(flow);
        fLowExecutor.executeFlow(flowTestExecution);
        System.out.println("Flow Ended with "+ flowTestExecution.getFlowExecutionResult());

    }
    @Override
    public void displayMenu2() {
        System.out.println("Flow Chooser Menu:");
        // Stepper stepperData = DataManager::getData();
        for (MainMenuItems item : MainMenuItems.values()) {
            if (item == MainMenuItems.MAIN_MENU) {//no need to present main menu option in the Main Menu
                continue;
            }
            System.out.println(item.ordinal() + " - " + item.getName());
        }
    }
}
