package Menu;

import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.dataDefinition.impl.mapping.Mapping;
import modules.dataDefinition.impl.relation.RelationData;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.FlowExecutionResult;
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
        try {
            int choice = input.nextInt();
            if (0 > choice || choice > stepperData.getFlows().size()) {
                throw new MenuException(MenuExceptionItems.EMPTY, "The number you chose is not in range");
            }
            if (choice == MainMenuItems.MAIN_MENU.getValue()) {
                return;
            }
            FlowDefinition flow = stepperData.getFlows().get(choice - 1);
            getUserInput(flow);
            ExecuteFlow(stepperData, choice);

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
    private int validInputCheck(String prompt, Map<Integer, Pair<String, DataDefinitionDeclaration>> options) {
        Scanner input = new Scanner(System.in);
        int choice;

        System.out.println(prompt);
        int i = 1;
        for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : options.values()) {
            System.out.println("(" + i + ")" + pairOfStringAndDD.getKey());
            i++;
        }

        while (true) {
            choice = input.nextInt();
            if (options.containsKey(choice)) {
                return choice;
            } else {
                System.out.println("Invalid input. Please enter a valid choice.");
            }
        }
    }


    private static void getUserInput(FlowDefinition flow) {
        String prompt = "Choose what to insert \n1.Mandatory inputs \n2.Optional inputs \n3. Done- and Execute ";

        System.out.println("for Flow :" + flow.getName() + prompt);
        List<Pair<String, DataDefinitionDeclaration>> freeInputRemain = new ArrayList<>();
        freeInputRemain.addAll(flow.getFlowFreeInputs());
        System.out.println(freeInputRemain.size());
        Scanner input = new Scanner(System.in);

        do {
            Map<Integer, Pair<String, DataDefinitionDeclaration>> dataOptions = new HashMap<>();
            int i,choice;
            try {
                choice = input.nextInt();

            if (choice < 1 || choice > 3) {
                if(choice == 0)
                    return;
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                continue; // go back to the start of the loop
            }
            switch (choice) {
                case 1:
                    if (!stillGotFreeManInputs(freeInputRemain)) {
                        System.out.println("No more free Mandatory inputs");
                        break;
                    }
                    System.out.println("Choose one to insert:");
                    i = 1;
                    for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
                        if (pairOfStringAndDD.getValue().isMandatory()) {
                            dataOptions.put(i, pairOfStringAndDD);
                            System.out.println("(" + i + ")" + pairOfStringAndDD.getKey());
                            i++;
                        }
                    }
                    choice = input.nextInt();
                    if(choice == 0)
                        return;
                    // Validate input can only be a num between 1 to i, prompt user to re-enter if invalid
                    while (choice < 1 || choice >= i) {

                        System.out.println("Invalid input. Please enter a number between 1 and " + (i - 1) + ".");
                        choice = input.nextInt();
                        if(choice == 0)
                            return;
                        if (choice < 1 || choice >= i) {
                            System.out.println("Invalid input. Please enter a number between 1 and " + (i - 1) + ".");
                        }
                    }
                    updateFreeInputs(flow, dataOptions.get(choice));
                    freeInputRemain.remove(dataOptions.get(choice));
                    //mistaber she no need in remove

                    System.out.println("For flow :" + flow.getName() +prompt);
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
                            System.out.println("(" + i + ")" + pairOfStringAndDD.getKey());
                            i++;
                        }
                    }

                    choice = input.nextInt();
                    if(choice == 0)
                        return;
                    // Validate input can only be a num between 1 to i, prompt user to re-enter if invalid
                    while (choice < 1 || choice >= i) {
                        System.out.println("Invalid input. Please enter a number between 1 and " + (i - 1) + ".");
                        choice = input.nextInt();
                    }
                    updateFreeInputs(flow, dataOptions.get(choice));
                   // freeInputRemain.remove(dataOptions.get(choice));
                    System.out.println("For flow :" + flow.getName() + prompt);
                    break;
                case 3:
                    if (stillGotFreeManInputs(freeInputRemain)) {
                        System.out.println("You must insert all mandatory inputs");
                        System.out.println("For flow : " + flow.getName() +" "+ prompt);
                    } else{
                        System.out.println("---Executing---");
                    }
                    break;

                default:
                    System.out.println("Wrong input");
                    System.out.println("For flow :" + flow.getName() + prompt);
            }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number. \n"+prompt);
            }// validate input num between 1-3 if not re-get it

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

        String userInput = input.nextLine();
        Pair<String,String> pairToAdd=new Pair<String,String>(value.getKey(), userInput);
        // Check if the key already exists in the list of user inputs
        for (Pair<String,String> pair : flow.getUserInputs()) {
            if (pair.getKey().equals(pairToAdd.getKey())) {
                flow.getUserInputs().remove(pair);
                break;
            }
        }
        flow.getUserInputs().add(pairToAdd);
        // If the pair doesn't exist, add it to the flow definition
    }


    private static void ExecuteFlow(Stepper stepperData,int choice) {
        //if there is no mandatory inputs that the user didn't insert throw exception!!!
        FlowDefinitionImpl flow = stepperData.getFlows().get(choice - 1);
        System.out.println("Executing flow: " + flow.getName());
        FLowExecutor fLowExecutor = new FLowExecutor();
        FlowExecution flowTestExecution = new FlowExecution(flow);
        fLowExecutor.executeFlow(flowTestExecution);
        stepperData.AddFlowExecution(flowTestExecution);
        //todo add user input to flow execution and clear the flow definition user inpout !!!!
        System.out.print("Done executing flow: " + flow.getName() + " \nID: " + flowTestExecution.getUniqueId() +
                " \nEnded with: " + flowTestExecution.getFlowExecutionResult());
        if (!flowTestExecution.getFlowExecutionResult().equals(FlowExecutionResult.FAILURE)) {
            System.out.println(" ,And the Flow outputs: ");
            PrintFormalOutput(flowTestExecution.getExecutionOutputs());
        }
        else
            System.out.println();
    }

    private static void PrintFormalOutput(Map<String, Object> executionOutputs) {
        for (Map.Entry<String, Object> entry : executionOutputs.entrySet()) {
            System.out.print(entry.getKey() + " : ");
            if (entry.getValue() instanceof RelationData)
                System.out.println();
            if (entry.getValue() instanceof String) {
                System.out.println(entry);
            }else if(entry.getValue() instanceof Mapping) {
                String fixed=entry.toString();
                fixed=fixed.replace(fixed.substring(0, fixed.indexOf("=")+1),"");
                System.out.println(fixed);
            }else if (entry.getValue() instanceof List) {
                System.out.println((entry.toString()));
            }else {
                System.out.println(entry.getValue().toString());
            }
        }
    }

    @Override
    public void displayMenu2() {
    }
}
