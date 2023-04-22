package modules.flow.definition.api;
import modules.dataDefinition.impl.DataDefinitionRegistry;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javafx.util.Pair;
import modules.step.api.DataNecessity;

public class FlowDefinitionImpl implements FlowDefinition {

    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private final List<StepUsageDeclaration> steps;
    private List<Pair<String,DataDefinitionDeclaration>> freeInputs;

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new ArrayList<>();
    }

    public void addFlowOutput(String outputName) {
        flowOutputs.add(outputName);
    }

    @Override
    public void validateFlowStructure() {
        //todo do some validation logic...
        createFlowFreeInputs();
    }
    @Override
    public List<Pair<String, DataDefinitionDeclaration>> getFlowFreeInputs() {return freeInputs;}
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<StepUsageDeclaration> getFlowSteps() {
        return steps;
    }

    @Override
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }

    public void createFlowFreeInputs() {
        List<DataDefinitionDeclaration> tempListInputs = new ArrayList<>();
        for (StepUsageDeclaration currentStep: steps) {//run on all steps
            //System.out.println(currentStep.getFinalStepName());
                List<DataDefinitionDeclaration> tempInput = currentStep.getStepDefinition().inputs();
                for(DataDefinitionDeclaration DD:tempInput) {

                    if (!valueExistsInList(tempListInputs,DD)){
                       // System.out.println(DD.getName());
                        freeInputs.add(new Pair<>(currentStep.getFinalStepName(),DD));
                    }
                }
                List<DataDefinitionDeclaration> tempOutput = currentStep.getStepDefinition().outputs();
                //System.out.println(currentStep.getStepDefinition().outputs().size());
                for (DataDefinitionDeclaration DDOut:tempOutput) {
                 //   System.out.println(DDOut.getName());
                    tempListInputs.add(DDOut);
                }
            }
        }
    public boolean valueExistsInList(List<DataDefinitionDeclaration> myList, DataDefinitionDeclaration valueToFind) {
        for (DataDefinitionDeclaration obj : myList) {
            if (obj.getName() == valueToFind.getName() ){
                return true;
            }
        }
        return false;
    }
    public StepExecutionContext setFreeInputs(StepExecutionContext context) {
        System.out.println("Please fill the free inputs\n");
        Scanner myScanner = new Scanner(System.in);
        String dataToStore;
        for (Pair<String,DataDefinitionDeclaration> pairOfStringAndDD : freeInputs) {
            System.out.println("The Step is: "+pairOfStringAndDD.getKey() +" The DD is: " +
                    pairOfStringAndDD.getValue().getName() + " The Necessity is " + pairOfStringAndDD.getValue().necessity()
                    + " Please enter a " + pairOfStringAndDD.getValue().dataDefinition().getName());
            if (pairOfStringAndDD.getValue().getName() == "LINE"){
                int num = myScanner.nextInt();
                context.storeDataValue(pairOfStringAndDD.getValue().getName(),num);
            }else {
                dataToStore = myScanner.nextLine();
                if (!dataToStore.isEmpty()) {
                    context.storeDataValue(pairOfStringAndDD.getValue().getName(),dataToStore);
                }
            }
        }
        return context;
        //todo need to move this to the UI
        //todo :need to get input by identification of the type of the data 4example: int, List<FileData> etc.
        // todo check if the data that the user enter is the same type of the real data how i need to get
        //todo check if there is any conversion from string to int

    }
}
