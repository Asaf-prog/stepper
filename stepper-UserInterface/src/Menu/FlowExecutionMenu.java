package Menu;

import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;

import java.util.*;

public class FlowExecutionMenu implements Menu {

    public static void displayMenu()throws Exception{
        Stepper stepperData = DataManager.getData();
        System.out.println("---Flow Chooser Menu---");
        int i=1;
        for(FlowDefinitionImpl flow : stepperData.getFlows()){
            System.out.println("("+i+")"+ flow.getName() + " That does: " + flow.getDescription());

            i++;
        }
        Scanner input = new Scanner(System.in);
        //todo validate input
        int choice = input.nextInt();
       try{ if (0>choice || choice> stepperData.getFlows().size()){
            throw new MenuException(MenuExceptionItems.EMPTY,"The number you chose is not in range");
        }
        if (choice== MainMenuItems.MAIN_MENU.getValue()){
            return;
        }
            FlowDefinition flow =stepperData.getFlows().get(choice-1);
            getUserInput(flow);
            ExecuteFlow(stepperData,choice);
        }
        catch (Exception e){
           if (e instanceof InputMismatchException)
               throw new MenuException(MenuExceptionItems.EMPTY,"we expected Number... ");

           else {//todo Class Exception for ExecutionException
               System.out.println(e.getMessage());
               return;
           }
        }
    }

    private static void getUserInput(FlowDefinition flow) {

        System.out.println("for Flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
        List<Pair<String, DataDefinitionDeclaration>> freeInputRemain = new ArrayList<>();
        freeInputRemain.addAll(flow.getFlowFreeInputs());
        Scanner input = new Scanner(System.in);
        do {
            Map<Integer,Pair<String, DataDefinitionDeclaration >> dataOptions = new HashMap<>();
            int i,choice;
            choice = input.nextInt();
        //todo validate input
            switch (choice) {
                case 1:
                    if (!stillGotFreeManInputs(freeInputRemain)) {
                        System.out.println("No more free Mandatory inputs");
                        break;
                    }//else
                    System.out.println("Choose one to insert:");
                    i=1;
                    for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
                        if (pairOfStringAndDD.getValue().isMandatory()) {
                            dataOptions.put(i, pairOfStringAndDD);
                            System.out.println("("+i + ")" + pairOfStringAndDD.getKey());
                            i++;
                        }
                    }
                    choice= input.nextInt();
                    //todo validate input
                    updateFreeInputs(flow, dataOptions.get(choice));//maybe add field in flow that hold user insertions for execution
                    freeInputRemain.remove(dataOptions.get(choice));//remove the free input the inserted
                    System.out.println("For flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
                    //assume it works and now one less data to update
                    break;
                case 2:
                    if(noMoreOptionalInputs(freeInputRemain)){
                        System.out.println("No more free Optional inputs");
                        break;
                    }
                    System.out.println("Choose input to insert:");
                    i=1;
                    for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
                        if (!pairOfStringAndDD.getValue().isMandatory()) {
                            dataOptions.put(i, pairOfStringAndDD);
                            System.out.println("("+i + ")" + pairOfStringAndDD.getKey());
                            i++;
                        }
                    }
                    choice= input.nextInt();
                    //todo validate input
                    updateFreeInputs(flow, dataOptions.get(choice));//maybe add field in flow that hold user insertions for execution
                    freeInputRemain.remove(dataOptions.get(choice));//remove the free input the inserted
                    System.out.println("For flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
                    //free inputs print all optional
                    break;
                case 3:
                    if (stillGotFreeManInputs(freeInputRemain)) {
                        System.out.println("You must insert all mandatory inputs");
                        System.out.println("For flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
                        break;
                    }//else
                    System.out.println("---Executing---");
                    //maybe update Free-inputs
                    return;
                default:
                    System.out.println("Wrong input");
                    System.out.println("For flow :" + flow.getName() + "Choose what to insert \n 1.Mandatory inputs \n 2.Optional inputs \n 3. Done- and Execute ");
                    break;
            }
        }while (freeInputRemain.size() > 0) ;
    }

    private static boolean noMoreOptionalInputs(List<Pair<String, DataDefinitionDeclaration>> freeInputRemain) {
        for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
            if (!pairOfStringAndDD.getValue().isMandatory()) {
                return false;
            }
        }
        return true;
    }
    private static boolean stillGotFreeManInputs(List<Pair<String, DataDefinitionDeclaration>> freeInputRemain) {
        for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
            if (pairOfStringAndDD.getValue().isMandatory()) {
                return true;
            }
        }
        return false;
    }

    private static void updateFreeInputs(FlowDefinition flow, Pair<String, DataDefinitionDeclaration> value) {
        System.out.println("Insert value for " + value.getValue().getFinalName());
        Scanner input = new Scanner(System.in);
        //todo check if the input is matching the data definition
        String userInput = input.nextLine();
        flow.getUserInputs().add(new Pair<String,String>(value.getKey(), userInput));
    }

    private static void ExecuteFlow(Stepper stepperData,int choice) {
        //if there is no mandatory inputs that the user didn't insert throw exception!!!
        FlowDefinitionImpl flow= stepperData.getFlows().get(choice-1);
        System.out.println("Executing flow: "+ flow.getName());
        FLowExecutor fLowExecutor = new FLowExecutor();
        FlowExecution flowTestExecution = new FlowExecution(flow);
        fLowExecutor.executeFlow(flowTestExecution);
        stepperData.AddFlowExecution(flowTestExecution);
        System.out.println("Done executing flow: "+ flow.getName() + " \n ID: "+ flowTestExecution.getUniqueId() +
                " \nEnded with: "+ flowTestExecution.getFlowExecutionResult());
         System.out.println("Flow outputs: ");
         PrintFormalOutput(flowTestExecution.getExecutionOutputs());
    }

    private static void PrintFormalOutput(Map<String, Object> executionOutputs) {
        for (Map.Entry<String, Object> entry : executionOutputs.entrySet()) {
            System.out.println(entry.getKey() + " : \n" + entry.getValue());
        }
    }

    @Override
    public void displayMenu2() {
    }
}
