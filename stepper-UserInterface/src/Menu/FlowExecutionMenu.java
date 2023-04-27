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

import java.util.*;

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

        System.out.println("for Flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
        List<Pair<String, DataDefinitionDeclaration>> freeInputRemain = new ArrayList<>();
        freeInputRemain.addAll(flow.getFlowFreeInputs());
        while (freeInputRemain.size() > 0) {
            Scanner input = new Scanner(System.in);
            Map<Integer, DataDefinitionDeclaration>dataOptions = new HashMap<>();
            int choice = input.nextInt();
            int i = 1;
            switch (choice) {
                case 1:
                    System.out.println("Choose one to insert:");
                    for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
                        if (pairOfStringAndDD.getValue().isMandatory()) {
                            dataOptions.put(i, pairOfStringAndDD.getValue());
                            System.out.println(i + ". " + pairOfStringAndDD.getKey());
                            i++;
                        }
                    }
                    input.nextInt();
                    UpdateFreeInputs(flow, dataOptions.get(choice));//maybe add field in flow that hold user insertions for execution
                    freeInputRemain.remove(dataOptions.get(choice));//remove the free input the inserted
                    System.out.println("for Flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
                    //assume it work and now one less data to update
                    break;
                case 2:
                    System.out.println("Choose one to insert:");
                    for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
                        if (!pairOfStringAndDD.getValue().isMandatory()) {
                            dataOptions.put(i, pairOfStringAndDD.getValue());
                            System.out.println(i + ". " + pairOfStringAndDD.getKey());
                        }
                    }
                    input.nextInt();
                    UpdateFreeInputs(flow, dataOptions.get(choice));//maybe add field in flow that hold user insertions for execution
                    freeInputRemain.remove(dataOptions.get(choice));//remove the free input the inserted
                    System.out.println("for Flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
                    //free inputs print all optional
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Wrong input");
                    System.out.println("for Flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
                    break;
            }
        }
    }

    private static void UpdateFreeInputs(FlowDefinition flow, DataDefinitionDeclaration value) {
        System.out.println("Insert value for " + value.getName());
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        flow.getUserInputs().put(value, userInput);
    }

    private static void ExecuteFlow(Stepper stepperData,int choice) {
        //if there is no mandatory inputs that the user didn't insert throw exception!!!
        FlowDefinitionImpl flow= stepperData.getFlows().get(choice-1);
        System.out.println("Executing flow: "+ flow.getName());

        flow.validateFlowStructure();
        FLowExecutor fLowExecutor = new FLowExecutor();

        FlowExecution flowTestExecution = new FlowExecution(flow);
        fLowExecutor.executeFlow(flowTestExecution);
        System.out.println("Flow Ended with "+ flowTestExecution.getFlowExecutionResult());

    }
    @Override
    public void displayMenu2() {
    }
}
