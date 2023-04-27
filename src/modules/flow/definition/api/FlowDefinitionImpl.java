package modules.flow.definition.api;
import modules.Map.AutomaticMapping;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;

public class FlowDefinitionImpl implements FlowDefinition {

    protected final String name;
    protected final String description;
    protected final List<String> flowOutputs;

    protected final List<StepUsageDeclaration> steps;
    protected List<CustomMapping> customMappings;
    protected  List<FlowLevelAlias> flowLevelAliases;
    protected List<Pair<String,DataDefinitionDeclaration>> freeInputs;
    protected boolean isCustomMappings;
    protected static int timesUsed;
    protected static double avgTime;
    protected boolean readOnly;
    //todo add a boolean filed how check if it's automaticMappings or customMappings





    @Override
    public List<FlowLevelAlias> getFlowLevelAlias(){return flowLevelAliases;}
    public void setFlowLevelAliases(List<FlowLevelAlias> flowLevelAliases) {
        this.flowLevelAliases = flowLevelAliases;
    }

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new ArrayList<>();
        customMappings = new ArrayList<>();
        flowLevelAliases = new ArrayList<>();
        readOnly=true;
        timesUsed=0;
        avgTime=0;
    }

    public static int getTimesUsed() {
        return timesUsed;
    }
    @Override
    public void addUsage() {
            FlowDefinitionImpl.timesUsed++;
    }

    @Override
    public double getAvgTime() {return avgTime;}

    @Override
    public double updateAvgTime(Duration time) {
        avgTime = (avgTime * (timesUsed-1) + time.toMillis()) / timesUsed;
        return avgTime;
    }
    public StepUsageDeclaration getStepByName(String name){
        for(StepUsageDeclaration tempStep: steps){
            if(tempStep.getFinalStepName().equals(name)){
                return tempStep;
            }
        }
        return null;
    }
    public void setIsCustomMappings(boolean isCustomMappings){this.isCustomMappings = isCustomMappings;}
    @Override
    public boolean getIsCustomMappings(){return isCustomMappings;}

    public void addFlowOutput(String outputName) {flowOutputs.add(outputName);}

    @Override
    public void validateFlowStructure() {
        //todo validate of flow structure base on step location,legal customMappings etc.

        // createFlowFreeInputs();
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
    public boolean stepExistInListOFCustomMapping(String step) {
        for (CustomMapping tempCustomMapping : customMappings) {
            if (tempCustomMapping.getTarget().equals(step))
                return true;
        }
        return false;
    }
    public void createFlowFreeInputs() {
        List<DataDefinitionDeclaration> ListInputs = new ArrayList<>();

    }
    public StepExecutionContext setFreeInputs(StepExecutionContext context) {
        System.out.println("Please fill the free inputs\n");
        Scanner myScanner = new Scanner(System.in);
        String dataToStore;
        for (Pair<String,DataDefinitionDeclaration> pairOfStringAndDD : freeInputs) {
            System.out.println("The Step is: "+pairOfStringAndDD.getKey() +" The DD is: " +
                    pairOfStringAndDD.getValue().getFinalName() + " The Necessity is " + pairOfStringAndDD.getValue().necessity()
                    + " Please enter a " + pairOfStringAndDD.getValue().dataDefinition().getName());

            if (pairOfStringAndDD.getValue().getName() == "LINE"){
                int num = myScanner.nextInt();
                context.storeDataValue(pairOfStringAndDD.getValue().getFinalName(),num);
            }else {
                dataToStore = myScanner.nextLine();
                if (!dataToStore.isEmpty()) {
                    context.storeDataValue(pairOfStringAndDD.getValue().getFinalName(),dataToStore);
                }
            }
        }
        return context;
        //todo need to move this to the UI
        //todo :need to get input by identification of the type of the data 4example: int, List<FileData> etc.
        // todo check if the data that the user enter is the same type of the real data how i need to get
        //todo check if there is any conversion from string to int

    }
    public List<String> getFlowOutputs() {return flowOutputs;}

    public List<StepUsageDeclaration> getSteps() {return steps;}
    @Override
    public List<CustomMapping> getCustomMappings() {return customMappings;}

    public void setCustomMappings(List<CustomMapping> customMappings) {
        this.customMappings = customMappings;
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

    public void setReadOnlyState() {
        for (StepUsageDeclaration step: steps) {
            if (step.getStepDefinition().isReadonly() == false){
                readOnly = false;
            }
        }
    }

    public boolean IsReadOnly() {
        return readOnly;
    }

   public void setFinalNames(){
        //going through all the Aliases and then custom mappings and setting the final names
        setAliases();
        setCustomMappingConnections();
   }

    private void setCustomMappingConnections() {

    }

    public void setAliases() {
        for (FlowLevelAlias alias: flowLevelAliases) {
            StepUsageDeclaration step = getStepByNameFromSteps(alias.getSource(), steps);
            for (DataDefinitionDeclaration DD : step.getStepDefinition().inputs()) {
                if (DD.getName().equals(alias.getSourceData())) {
                    DD.setFinalName(alias.getAlias());
                }
            }
            for (DataDefinitionDeclaration DD : step.getStepDefinition().outputs()) {
                if (DD.getName().equals(alias.getSourceData())) {
                    DD.setFinalName(alias.getAlias());
                }
            }
        }
    }
    private StepUsageDeclaration getStepByNameFromSteps(String source, List<StepUsageDeclaration> steps) {

        for (StepUsageDeclaration step: steps) {
            if (step.getFinalStepName().equals(source))
                return step;
        }
        return null;
    }


}
