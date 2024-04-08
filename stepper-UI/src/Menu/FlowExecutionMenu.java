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
import modules.step.api.DataNecessity;
import modules.stepper.Stepper;

import java.util.*;

public class FlowExecutionMenu {

    public static void displayMenu() throws Exception {
        Stepper stepperData = DataManager.getData();
        System.out.println("---Flow Chooser Menu---");
        while(true)
            {
            int i = 1;
            for (FlowDefinitionImpl flow : stepperData.getFlows()) {
                System.out.println("(" + i + ") " + flow.getName() + " That does: " + flow.getDescription());

                i++;
            }
            Scanner input = new Scanner(System.in);
            try {
                int choice = input.nextInt();
                if (0 > choice || choice > stepperData.getFlows().size())
                    continue;
                if (choice == MainMenuItems.MAIN_MENU.getValue())
                    return;
                FlowDefinition flow = stepperData.getFlows().get(choice - 1);
                boolean back = getUserInput(flow);
                if (!back) {
                    System.out.println("Choose flow to execute");
                    continue;//back to main menu
                }
                else
                    ExecuteFlow(stepperData, choice);

                return;
            } catch (InputMismatchException e) {
                    input.next();
                    System.out.println("Invalid input, please try again");
                    continue;
                }
           catch (Exception e) {
                    throw new MenuException(MenuExceptionItems.EMPTY, " Error in flow Execution");
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

    private static boolean getUserInput(FlowDefinition flow) throws MenuException {
        String prompt = "Choose what to insert \n(0) Back \n(1) Mandatory inputs \n(2) Optional inputs \n(3) Done- and Execute ";
        System.out.println("for Flow :" + flow.getName() + " " + prompt);
        List<Pair<String, DataDefinitionDeclaration>> freeInputRemain = new ArrayList<>(flow.getFlowFreeInputs());
        List<Pair<String, DataDefinitionDeclaration>> mandatoryDataOptions = GetDataOptions(freeInputRemain, DataNecessity.MANDATORY);
        List<Pair<String, DataDefinitionDeclaration>> optionalDataOptions = GetDataOptions(freeInputRemain, DataNecessity.OPTIONAL);
        Scanner input = new Scanner(System.in);
        boolean done = false;
        boolean finishInsert = false;
        do {
            int i, choice;
            try {
                String userIn = input.nextLine();
                Optional<Integer> possibleInt = Optional.of(Integer.parseInt(userIn));

                if (!possibleInt.isPresent()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    continue;
                }
                choice = possibleInt.get();
                if (choice < 1 || choice > 3) {
                    if (choice == 0)
                        return false;
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    continue; // go back to the start of the loop
                }
            } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 3.");
            continue;
            }
            switch (choice) {
                case 1:
                    done = false;
                    while(!done) {
                        System.out.println("Choose one to insert:");
                        i = 1;
                        for (Pair<String, DataDefinitionDeclaration> option : mandatoryDataOptions) {
                            System.out.println("(" + i + ") " + option.getKey());
                            i++;
                        }
                       // System.out.println("(" + i + ") Back");
                        try {
                            String userIn2 = input.nextLine();
                            Optional<Integer> possibleInt2 = Optional.of(Integer.parseInt(userIn2));
                            if (!possibleInt2.isPresent()) {
                                System.out.println("Invalid input. Please enter a number between 1 and "+(i-1)+".");
                                continue;
                            }
                            choice = possibleInt2.get();
                            if (choice < 1 || choice > i) {
                                if (choice == 0) {
                                    System.out.println(prompt);
                                    break;
                                }
                                System.out.println("Invalid input. Please enter a number between 1 and "+(i-1)+".");
                                continue; // go back to the start of the loop
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number between 1 and "+(i-1)+".");
                            continue;
                        }
                        updateFreeInputs(flow, mandatoryDataOptions.get(choice - 1));
                        freeInputRemain.remove(mandatoryDataOptions.get(choice - 1));
                        done = true;
                        System.out.println("For flow :" + flow.getName() + " " + prompt);
                        break;
                    }
                    break;
                case 2:
                    done = false;
                    while(!done) {
                        System.out.println("Choose one to insert:");
                        i = 1;
                        for (Pair<String, DataDefinitionDeclaration> option : optionalDataOptions) {
                            System.out.println("(" + i + ") " + option.getKey());
                            i++;
                        }

                        try {
                            String userIn3 = input.nextLine();
                            Optional<Integer> possibleInt3 = Optional.of(Integer.parseInt(userIn3));
                            if (!possibleInt3.isPresent()) {
                                System.out.println("Invalid input. Please enter a number between 1 and "+(i-1)+".");
                                continue;
                            }
                            choice = possibleInt3.get();
                            if (choice < 1 || choice > i) {
                                if (choice == 0) {
                                    System.out.println(prompt);
                                    break;
                                }
                                System.out.println("Invalid input. Please enter a number between 1 and "+(i-1)+".");
                                continue; // go back to the start of the loop
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number between 1 and "+(i-1)+".");
                            continue;
                        }
                        updateFreeInputs(flow, optionalDataOptions.get(choice - 1));
                        freeInputRemain.remove(optionalDataOptions.get(choice - 1));
                        done = true;
                        System.out.println("For flow :" + flow.getName() + " " + prompt);
                        break;
                    }
                    break;
                case 3:
                    if (stillGotFreeManInputs(freeInputRemain)) {
                        System.out.println("You must insert all mandatory inputs");
                        System.out.println("For flow : " + flow.getName() + " " + prompt);
                    } else
                        finishInsert = true;
                    break;
                default:
                    System.out.println("Wrong input");
                    System.out.println("For flow :" + flow.getName() + prompt);
            }
            if (finishInsert) {
                return true;
            }
        } while (freeInputRemain.size() > 0 || !finishInsert);
        return true;
    }

    private static List<Pair<String, DataDefinitionDeclaration>> GetDataOptions
            (List<Pair<String, DataDefinitionDeclaration>> freeInputRemain, DataNecessity necessity) {
        List<Pair<String, DataDefinitionDeclaration>> dataOptions = new ArrayList<>();
        for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
            if (pairOfStringAndDD.getValue().necessity() == necessity) {
                dataOptions.add(pairOfStringAndDD);
            }
        }
        return dataOptions;
    }

    private static boolean stillGotFreeOptionalInputs(List<Pair<String, DataDefinitionDeclaration>> freeInputRemain) {
        for (Pair<String, DataDefinitionDeclaration> pairOfStringAndDD : freeInputRemain) {
            if (!pairOfStringAndDD.getValue().isMandatory()) {
                return true;
            }
        }
        return false;
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
        System.out.println("Insert value for " + value.getKey()+":");
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
        FlowExecution flowTestExecution = null;
        try {
            //if there is no mandatory inputs that the user didn't insert throw exception!!!
            FlowDefinitionImpl flow = stepperData.getFlows().get(choice - 1);
            System.out.println("Executing flow: " + flow.getName());
            FLowExecutor fLowExecutor = new FLowExecutor();
            flowTestExecution = new FlowExecution(flow);
            fLowExecutor.executeFlow(flowTestExecution);
            stepperData.addFlowExecution(flowTestExecution);
            System.out.print("Done executing flow: " + flow.getName() + " \nID: " + flowTestExecution.getUniqueId() +
                    " \nEnded with: " + flowTestExecution.getFlowExecutionResult());
            if (!flowTestExecution.getFlowExecutionResult().equals(FlowExecutionResult.FAILURE)) {
                if(flowTestExecution.getExecutionOutputs().size()>0) {
                    System.out.println(" ,And the Flow outputs: ");
                    PrintFormalOutput(flowTestExecution.getExecutionOutputs());
                }else{
                    System.out.println(" ,No outputs were generated during the flow execution.");
                }
                System.out.println("Press enter twice to continue :)");
                Scanner input = new Scanner(System.in);
                input.nextLine();
                return;
            } else
                System.out.println();
        } catch (Exception e) {
            System.out.println("Error executing flow: ");
            stepperData.addFlowExecution(flowTestExecution);
            return;
        }
    }

    private static void PrintFormalOutput(Map<String, Object> executionOutputs) {
        for (Map.Entry<String, Object> entry : executionOutputs.entrySet()) {

            System.out.println(entry.getKey() + " : ");

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

}
