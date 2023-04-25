package Menu;

import modules.DataManeger.DataManager;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.stepper.Stepper;
import Menu.MainMenuItems;
import java.util.Scanner;

public class FlowDefinitionMenu implements Menu{

    public static void displayMenu(){

        System.out.println("Choose a flow to get its information:");
        Stepper stepperData = DataManager.getData();
        int i=1;
        for(FlowDefinitionImpl flow : stepperData.getFlows()){
            System.out.println(i+". "+ flow.getName() + "    ,which " + flow.getDescription());
            i++;
        }
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice== MainMenuItems.MAIN_MENU.getValue()){
            return;
        }
        try{
            PresentFlowInformation(stepperData.getFlows().get(choice-1));
        }
        catch (Exception e){
            System.out.println(" Opps,no can do!");
            return;
        }
        return;
    }

    private static void PresentFlowInformation(FlowDefinitionImpl flow) {
        System.out.println("Flow name: " + flow.getName());
        System.out.println("Flow description: " + flow.getDescription());

        System.out.println("Steps Formal Outputs: " + flow.getFlowOutputs());
        System.out.println("Is the Step ReadOnly? " + flow.IsReadOnly());

        System.out.println("Flow steps: ");
        int i=1;
        for(StepUsageDeclaration step : flow.getSteps()){
            System.out.println(i+". "+ step.getFinalStepName());
            i++;
            PrintStepInformation(step);
        }
        System.out.println(" that's it! for now... ");
    }

    private static void PrintStepInformation(StepUsageDeclaration step) {
        System.out.println("Step name: " + step.getFinalStepName());
        System.out.println("Step inputs: ");
        System.out.println("Step Skip if Fail? : "+step.skipIfFail());
    }

    @Override
    public void displayMenu2() {
    }
}
