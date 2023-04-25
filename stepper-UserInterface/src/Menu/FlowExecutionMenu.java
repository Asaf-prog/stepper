package Menu;

import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;
import modules.step.StepDefinitionRegistry;
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
        int choice = input.nextInt();
        ExecuteFlow(stepperData,choice);
    }
    private static void ExecuteFlow(Stepper stepperData,int choice) {

        System.out.println("Executing flow: "+ stepperData.getFlows().get(choice-1).getName());

        // flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.COLLECT_FILES_IN_FOLDER.getStepDefinition()));
//        flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILES_CONTENT_EXTRACTOR.getStepDefinition()));
//        flow1.validateFlowStructure();
//
//
//        FLowExecutor fLowExecutor = new FLowExecutor();
//        FlowExecution flowTestExecution1 = new FlowExecution("19",flow1);

//        fLowExecutor.executeFlow(flowTestExecution1);


        FlowDefinitionImpl flow= stepperData.getFlows().get(choice-1);
       flow.validateFlowStructure();
        FLowExecutor fLowExecutor = new FLowExecutor();

        FlowExecution flowTestExecution = new FlowExecution(flow);
        fLowExecutor.executeFlow(flowTestExecution);
    }

    @Override
    public void displayMenu2() {
        System.out.println("FlowChooserMenu:");
        // Stepper stepperData = DataManager::getData();
        for (MainMenuItems item : MainMenuItems.values()) {
            if (item == MainMenuItems.MAIN_MENU) {//no need to present main menu option in the Main Menu
                continue;
            }
            System.out.println(item.ordinal() + " - " + item.getName());
        }
    }
}
