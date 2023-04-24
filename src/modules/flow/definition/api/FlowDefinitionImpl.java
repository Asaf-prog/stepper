package modules.flow.definition.api;
import modules.Map.AutomaticMapping;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
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

    protected final String name;
    protected final String description;
    protected final List<String> flowOutputs;
    protected final List<StepUsageDeclaration> steps;
    protected List<CustomMapping> customMappings;
    protected List <AutomaticMapping> automaticMappings;
    protected final List<FlowLevelAlias> flowLevelAliases;
    protected List<Pair<String,DataDefinitionDeclaration>> freeInputs;
    //todo add a boolean filed how check if it's automaticMappings or customMappings


    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new ArrayList<>();
        customMappings = new ArrayList<>();
        automaticMappings = new ArrayList<>();
        flowLevelAliases = new ArrayList<>();

    }

    public void addFlowOutput(String outputName) {
        flowOutputs.add(outputName);
    }

    @Override
    public void validateFlowStructure() {
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

    public List<String> getFlowOutputs() {
        return flowOutputs;
    }

    public List<StepUsageDeclaration> getSteps() {
        return steps;
    }

    public List<CustomMapping> getCustomMappings() {
        return customMappings;
    }

    public void setCustomMappings(List<CustomMapping> customMappings) {
        this.customMappings = customMappings;
    }

    public List<AutomaticMapping> getAutomaticMappings() {
        return automaticMappings;
    }

    public void setAutomaticMappings(List<AutomaticMapping> automaticMappings) {
        this.automaticMappings = automaticMappings;
    }

    public List<FlowLevelAlias> getFlowLevelAliases() {
        return flowLevelAliases;
    }

    public List<Pair<String, DataDefinitionDeclaration>> getFreeInputs() {
        return freeInputs;
    }

    public void setFreeInputs(List<Pair<String, DataDefinitionDeclaration>> freeInputs) {
        this.freeInputs = freeInputs;
    }
}
