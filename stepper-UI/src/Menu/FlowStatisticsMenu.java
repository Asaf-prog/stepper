package Menu;

import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.dataDefinition.impl.file.FileData;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;

import java.util.*;

public class FlowStatisticsMenu {
    public static void displayMenu() throws MenuException {

        Stepper stepperData = DataManager.getData();
        if(stepperData==null) {
            throw new MenuException(MenuExceptionItems.EMPTY, "No Data Loaded");
        }

        System.out.println("---Flow Statistics Menu---");
        System.out.println("Choose a flow to get its Stats:");

        int i = 1;

        if (stepperData.getFlowExecutions() == null) {
            System.out.println("No flow executions to show");
            return;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(0) Back\n");
            for (FlowExecution exe : stepperData.getFlowExecutions()) {
                String StartTime = exe.getStartDateTime();
                sb.append("(").append(i).append(") ").append(exe.getFlowDefinition().getName()).append("  Occurred on ").append(StartTime).append("\n");
                i++;
            }
            String menuString = sb.toString();
            System.out.println(menuString);
            Scanner input = new Scanner(System.in);
            while (true) {//loop until user choose back
                try {//try to get user input
                    Optional<Integer> choiceTry = Optional.of(input.nextInt());
                    if (!choiceTry.isPresent())
                        throw new MenuException(MenuExceptionItems.INVALID_NUMBER_INPUT, " wrong input for choice in Flow Statistics Menu");

                    int choice = choiceTry.get();
                    if (choice == MainMenuItems.MAIN_MENU.getValue())
                        return;

                    if (choice > stepperData.getFlowExecutions().size() || choice < 1) {
                        System.out.println("No such option , try again");
                        continue;
                    }

                    PresentFlowStats(stepperData.getFlowExecutionById(stepperData.getFlowExecutions().get(choice - 1).getUniqueId()));//present flow stats
                    System.out.println("Choose a flow to get its Stats:");
                    System.out.println(menuString);//print menu again when coming back from presenting flow stats
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input, please try again");
                    input.next();
                } catch (Exception e) {//catch any other exception
                    throw new MenuException(MenuExceptionItems.EMPTY, " Flow Statistics Menu");
                }
            }
        }
    }

    private static void PresentFlowStats(FlowExecution singleExecution) {//present flow stats
        System.out.println("Flow name: " + singleExecution.getFlowDefinition().getName());
        System.out.println("ID: " + singleExecution.getUniqueId());
        System.out.println("And finish with " + singleExecution.getFlowExecutionResult().name());
        System.out.println("Took about " + singleExecution.getTotalTime().toMillis() + " MS");
        //specific flow exe free inputs that inserted by user
        Scanner input = new Scanner(System.in);
        String menuOptions = "Please choose an option:\n" +
                "(0) Back\n" +
                "(1) Show information about all free inputs in the flow\n" +
                "(2) Show information about all outputs in the flow\n" +
                "(3) Show step execution statistics\n"+
                "(4) Show all flow logs list\n";
        int choice=69;
        while(choice!=0) {
            System.out.print(menuOptions);
            while (true) {
                try {
                    choice = input.nextInt();
                    if (choice < 0 || choice > 4) {
                        System.out.println("No such option, try again");
                        continue;
                    }
                    else
                        break;
                } catch (InputMismatchException e) {
                    System.out.println("You need to enter a number,try again ");
                    input.next();
                    continue;
                }
            }
            try {
                switch (choice) {
                    case 0:
                        return;//main menu
                    case 1:
                        presentInformationOfAllFreeInputs(singleExecution);//show free inputs info
                        continue;
                    case 2:
                        presentInformationAboutOutputsInFlow(singleExecution);//show outputs info
                        continue;
                    case 3:
                        PresentStepExecutionStats(singleExecution);//show step execution stats
                        continue;
                    case 4:
                        presentAllFlowLogs(singleExecution);//show all flow logs
                        continue;
                    default:
                        System.out.println("No such option, try again");
                        break;

                }
            }catch (Exception e){
                System.out.println("Something went wrong, try again");
                continue;
            }
        }

    }

    private static void presentAllFlowLogs(FlowExecution singleExecution) {//as it sounds
        if (singleExecution.getLogs().isEmpty()){
            System.out.println("No logs to show");
            return;
        }
        int i=1;
        for (StepUsageDeclaration step : singleExecution.getFlowDefinition().getFlowSteps()){
            if (singleExecution.getLogs()==null){
                System.out.println(" No logs to show for this step");
                continue;
            }
            System.out.println("Step "+step.getFinalStepName()+" logs:");
            if (singleExecution.getLogs().size()==0){
                System.out.println("No logs for this step");
                continue;
            }
            for (Pair<String,String> log : singleExecution.getLogs().get(step.getFinalStepName())){
                System.out.println("("+i+") "+log.getKey()+" - "+log.getValue()+"\n");
                i++;
            }



        }
    }

    private static void presentInformationAboutOutputsInFlow(FlowExecution singleExecution) {
        int i=1;
        if (singleExecution.getFlowDefinition().getFlowOfAllStepsOutputs().isEmpty()){
            System.out.println("No outputs in this flow");
            return;
        }
        for(String outputKey: singleExecution.getFlowDefinition().getFlowOfAllStepsOutputs()){

            Object outputValue= singleExecution.getAllExecutionOutputs().get(outputKey);
            if(outputValue==null)
            {
                if (i==1)
                    System.out.println("All outputs are empty, probably because flow failed... ");
                else
                    System.out.println("No more outputs to show");
                return;
            }
            System.out.print("("+i+") ");
            System.out.print(outputKey);
            if(outputValue instanceof ArrayList){//print an arraylist
                System.out.print(" ,Type: List");
                System.out.println(" ,Content:  ");
                for (Object o : (ArrayList) outputValue) {
                    if (o instanceof FileData)
                        System.out.print(((FileData) o).getName()+ " , ");
                    else
                        System.out.print(o.toString()+ " , ");

                }
                System.out.println();
            }else if (outputValue instanceof List){
                System.out.println((List)outputValue);
            } else {
                System.out.print(" ,Type: " + outputValue.getClass().getSimpleName());
                System.out.println(" ,Content:\n" + outputValue.toString());
            }
            i++;
        }
    }

    private static void presentInformationOfAllFreeInputs(FlowExecution singleExecution){

        List<Pair<String, DataDefinitionDeclaration>> freeInputs= singleExecution.getFlowDefinition().getFlowFreeInputs();
        List<Pair<String,String>> freeFromUser = singleExecution.getUserInputs();
        int i=1;
        for (Pair<String, DataDefinitionDeclaration> freeInput: freeInputs){
            System.out.print("("+i+") ");
            System.out.print(freeInput.getKey()+(" ,Type: "+freeInput.getValue().dataDefinition().getTypeName()));
            if (freeInput.getValue().isMandatory()){
                System.out.print(" ,Is Mandatory ");
            }
            else{
                System.out.print(" ,Is Optional ");
            }
            String value = freeFromUserGetByVal(freeFromUser,freeInput.getKey());
            if (value == null) {
                System.out.println(" ,Content: Empty...");
            }
            else {
                System.out.println(" ,Content is:  " + value);
            }
            i++;
        }
        Scanner input = new Scanner(System.in);
        System.out.println("Press enter to continue...");
        input.nextLine();
    }
    private static String freeFromUserGetByVal(List<Pair<String,String>> freeFromUser ,String nameToFind){//get  free input by name
        for (Pair<String, String> pair : freeFromUser) {
            if (pair.getKey().equals(nameToFind)) {
                return pair.getValue();
            }
        }
        return null;
    }
    private static void PresentStepExecutionStats(FlowExecution singleExecution) {//present step execution stats
        System.out.println("Choose step to get his stats");
        int i = 1;
        Scanner input = new Scanner(System.in);
        for (StepUsageDeclaration step : singleExecution.getFlowDefinition().getFlowSteps()) {
            System.out.println("(" + i + ") " + step.getFinalStepName());
            i++;
        }
        while (true) {
            try {
                int choice = input.nextInt();
                if (choice == MainMenuItems.MAIN_MENU.ordinal())
                    return;
                if (choice > singleExecution.getFlowDefinition().getFlowSteps().size() || choice < 1) {
                    System.out.println("No such option, try again");
                    continue;
                }
                PresentStepStats(singleExecution.getFlowDefinition().getFlowSteps().get(choice - 1), singleExecution.getLogs(), singleExecution.getSummaryLines());
                return;
            } catch (Exception e) {
                if (e instanceof InputMismatchException) {
                    System.out.println("You need to enter a number,try again ");
                    input.next();
                } else {
                    System.out.println("Unknown error");
                    return;
                }
            }
        }
    }
    private static void PresentStepStats(StepUsageDeclaration stepUsageDeclaration, Map<String, List<Pair<String, String>>> logs, Map<String, String> summaryLines) {
        System.out.print("Step name: " + stepUsageDeclaration.getFinalStepName());
        System.out.println(" ,Took about: " +stepUsageDeclaration.getTotalTime().toMillis() + " MS");
        System.out.println("And finish with " + stepUsageDeclaration.getStepResult());
        if(logs.get(stepUsageDeclaration.getFinalStepName()) ==null){
            System.out.println("No logs for this step");
        }
        else
            ShowStepLogs(logs.get(stepUsageDeclaration.getFinalStepName()));
        if(summaryLines.get(stepUsageDeclaration.getFinalStepName()) ==null){
            System.out.println("No summary line for this step");
        }
        else {
            System.out.println("Summery line : " + summaryLines.get(stepUsageDeclaration.getFinalStepName()));
        }
        Scanner input = new Scanner(System.in);
        System.out.println("Press enter to continue...");
        input.nextLine();

    }

    private static void ShowStepLogs(List<Pair<String, String>> logsOfStep) {
        System.out.println("Logs:");
        int i= 1;
        for (Pair<String, String> pair : logsOfStep) {
            System.out.println("("+i+") "+pair.getKey() + "  -  " + pair.getValue());
            i++;
        }
    }


}
