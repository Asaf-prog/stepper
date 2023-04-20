package modules.flow.definition.api;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;

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
        for (StepUsageDeclaration currentStep: steps) {
            for (int i=0; i<currentStep.getStepDefinition().inputs().size() ; i++) {
                List<DataDefinitionDeclaration> tempInput = currentStep.getStepDefinition().inputs();
                for(DataDefinitionDeclaration DD:tempInput) {
                    if (!tempListInputs.contains(DD)){
                        freeInputs.add(new Pair<>(currentStep.getFinalStepName(),DD));
                    }
                    List<DataDefinitionDeclaration> tempOutput = currentStep.getStepDefinition().outputs();
                    for (DataDefinitionDeclaration DDOut:tempOutput) {
                        tempListInputs.add(DDOut);
                    }
                }
            }
        }
    }
    public StepExecutionContext setFreeInputs(StepExecutionContext context) {
        System.out.println("Please fill the free inputs\n");
        for (Pair<String,DataDefinitionDeclaration> DD : freeInputs) {
            System.out.println("The Step is: "+DD.getKey() +" The DD is: " + DD.getValue().getName() + " The Necessity " + DD.getValue().necessity()
                    + " Please enter a " + DD.getValue().dataDefinition().getName());

            Scanner myScanner = new Scanner(System.in);
            String dataToStore = myScanner.nextLine();

            //todo check if the data that the user enter is the same type of the real data how i need to get
            //todo check if their is any conversion from string to int

            context.storeDataValue(DD.getValue().getName(),DD.getValue());

        }
        return context;
    }
}
