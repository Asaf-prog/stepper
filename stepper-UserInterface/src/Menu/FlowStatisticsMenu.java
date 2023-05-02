package Menu;

import javafx.util.Pair;
import modules.DataManeger.DataManager;
import modules.dataDefinition.impl.file.FileData;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.step.api.DataDefinitionDeclaration;
import modules.stepper.Stepper;

import java.nio.file.Path;
import java.util.*;

public class FlowStatisticsMenu implements Menu {
    public static void displayMenu() throws MenuException {

        Stepper stepperData = DataManager.getData();
        //todo check if stepperData if so null and throw exception
        System.out.println("---Flow Statistics Menu---");
        System.out.println("Choose a flow to get its Stats:");
        int i=1;
        if (stepperData.getFlowExecutions()==null){
            System.out.println("No flow executions to show");
            return;
        } else {
            System.out.println("(0)Back");
            for (FlowExecution exe : stepperData.getFlowExecutions()) {
                String StartTime=exe.getStartDateTime();
                System.out.println("("+i + ")" + exe.getFlowDefinition().getName() + "  Occurred on " + StartTime);
                i++;
            }try {
            Scanner input = new Scanner(System.in);
            Optional<Integer> choiceTry = Optional.of(input.nextInt());
            if (!choiceTry.isPresent())
                throw new MenuException(MenuExceptionItems.INVALID_NUMBER_INPUT, " Flow Statistics Menu");
            int choice = choiceTry.get();
            if (choice == MainMenuItems.MAIN_MENU.getValue())
                return;
            PresentFlowStats(stepperData.getFlowExecutionById(stepperData.getFlowExecutions().get(choice - 1).getUniqueId()));
            } catch (Exception e) {
            if (e instanceof MenuException) {
                throw e;
            }
            else
                throw new MenuException(MenuExceptionItems.EMPTY, " Prob a missing information problem");//todo change it
            }
        }
    }
    private static void PresentFlowStats(FlowExecution singleExecution) {
        System.out.println("Flow name: " + singleExecution.getFlowDefinition().getName());
        System.out.println("ID: " + singleExecution.getUniqueId());
        System.out.println("And finish with " + singleExecution.getFlowExecutionResult().name());
        System.out.println("Took about " +singleExecution.getTotalTime().toMillis() + " MS");
        //specific flow exe free inputs that inserted by user
        presentInformationOfAllFreeInputs(singleExecution);//5
        presentInformationAboutOutputsInFlow(singleExecution);//6
        System.out.println("The flow Outputs are :\n" + singleExecution.printOutputs());
        PresentStepExecutionStats(singleExecution);
        //show step
    }

    private static void presentInformationAboutOutputsInFlow(FlowExecution singleExecution) {
        for(String outputKey: singleExecution.getFlowDefinition().getFlowOfAllStepsOutputs()){
            System.out.println("Name: "+outputKey);
            Object outputValue= singleExecution.getAllExecutionOutputs().get(outputKey);
            if(outputValue instanceof ArrayList){
                System.out.println("The type is: List");
           
                System.out.println("Content:\n");
                for (Object o : (ArrayList) outputValue) {
                    if (o instanceof FileData)
                        System.out.print(((FileData) o).getName()+ " , ");
                    else
                    System.out.print(" , ");
                }
                System.out.println();
            }
            else {
                System.out.println("The type is: " + outputValue.getClass().getSimpleName());
                System.out.println("Content:\n" + outputValue.toString());
            }
        }
    }

    private static void presentInformationOfAllFreeInputs(FlowExecution singleExecution){

        List<Pair<String, DataDefinitionDeclaration>> freeInputs= singleExecution.getFlowDefinition().getFlowFreeInputs();
        List<Pair<String,String>> freeFromUser = singleExecution.getFlowDefinition().getUserInputs();

        for (Pair<String, DataDefinitionDeclaration> freeInput: freeInputs){
            System.out.println("Name: "+freeInput.getKey());
            System.out.println("The type is: "+freeInput.getValue().dataDefinition().getTypeName());

            String value = freeFromUserGetByVal(freeFromUser,freeInput.getKey());
            if (value == null) {
                System.out.println("Empty");
            }
            else {
                System.out.println("The content is:\n" + value);
            }
            if (freeInput.getValue().isMandatory()){
                System.out.println("This free input are mandatory. ");
            }
            else{
                System.out.println("This free input are optional. ");
            }
        }
    }
    private static String freeFromUserGetByVal(List<Pair<String,String>> freeFromUser ,String nameToFind){
        for (Pair<String, String> pair : freeFromUser) {
            if (pair.getKey().equals(nameToFind)) {
                return pair.getValue();
            }
        }
        return null;
    }
    private static void PresentStepExecutionStats(FlowExecution singleExecution) {
        System.out.println("Choose Step to get his stats");
        int i=1;
        Scanner input = new Scanner(System.in);
        for (StepUsageDeclaration step : singleExecution.getFlowDefinition().getFlowSteps()) {
            System.out.println("("+i+")"+step.getStepDefinition().getName());
            // valdiate input
            int choice = input.nextInt();
            PresentStepStats(singleExecution.getFlowDefinition().getFlowSteps().get(choice-1),singleExecution.getLogs(),singleExecution.getSummaryLines());
            i++;
        }
    }
    private static void PresentStepStats(StepUsageDeclaration stepUsageDeclaration, Map<String, List<Pair<String, String>>> logs, Map<String, String> summaryLines) {
        System.out.println("Step name: " + stepUsageDeclaration.getStepDefinition().getName());
        System.out.println("Took Exactly: " + stepUsageDeclaration.getTotalTime());
        System.out.println("And finish with " + stepUsageDeclaration.getStepResult());
        System.out.println("Took about " +stepUsageDeclaration.getTotalTime().toMillis() + " MS");
        ShowFlowLogs(logs.get(stepUsageDeclaration.getStepDefinition().getName()));
        System.out.println("Summery line : " + summaryLines.get(stepUsageDeclaration.getStepDefinition().getName()));

    }

    private static void ShowFlowLogs(List<Pair<String, String>> pairs) {
        for (Pair<String, String> pair : pairs) {
            System.out.println(pair.getKey() + "  -  " + pair.getValue());
        }
    }

    @Override
    public void displayMenu2() {

    }
}
