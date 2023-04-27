package modules.flow.definition.api;
import modules.Map.AutomaticMapping;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.execution.context.StepExecutionContext;
import modules.flow.execution.getNameFromAliasDD.getNameFromAliasDDImpl;
import modules.flow.execution.getNameFromAliasStep.getNameFromAliasImpl;
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
    protected List <AutomaticMapping> automaticMappings;

    public void setFlowLevelAliases(List<FlowLevelAlias> flowLevelAliases) {
        this.flowLevelAliases = flowLevelAliases;
    }
    protected  List<FlowLevelAlias> flowLevelAliases;
    protected List<Pair<String,DataDefinitionDeclaration>> freeInputs;
    protected boolean isCustomMappings;
    protected static int timesUsed;
    protected static double avgTime;
    protected boolean readOnly;
    protected getNameFromAliasImpl mappingFromNameToAlias;
    public getNameFromAliasDDImpl mappingFromNameToAliasDD;
    //todo add a boolean filed how check if it's automaticMappings or customMappings

    @Override
    public List<FlowLevelAlias> getFlowLevelAlias(){return flowLevelAliases;}

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new ArrayList<>();
        customMappings = new ArrayList<>();
        automaticMappings = new ArrayList<>();
        flowLevelAliases = new ArrayList<>();
        mappingFromNameToAlias = new getNameFromAliasImpl();
        mappingFromNameToAliasDD = new getNameFromAliasDDImpl();
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
    @Override
    public getNameFromAliasImpl getMappingFromNameToAlias(){return mappingFromNameToAlias;}
    public void addAnewValToMapOfNamesByKey(String name, String alias){
        mappingFromNameToAlias.addNewNameToMap(name,alias);
    }
    public void addAnewValToMapOfNamesByKeyWithObject(StepUsageDeclarationImpl step,String name, String alias){
        mappingFromNameToAliasDD.addNewNameToMapWithObject(step,name,alias);
    }
    public String getStepByName(String name){return mappingFromNameToAlias.getValByKey(name);}
    public void addAnewValToMapOfNamesByKeyInDD(String name,String alias){
        mappingFromNameToAliasDD.addNewNameToMap(name,alias);
    }
    public String getStepByNameDD(String name){return mappingFromNameToAliasDD.getValByKey(name);}
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
    @Override
    public void setMappingForStep(){
        for(StepUsageDeclaration tempStep: steps){
            //if step exist in list of customMapping create to this step a map and initialized a boolean that represent if is custom or not
            if(stepExistInListOFCustomMapping(tempStep.getFinalStepName())){
                tempStep.isCustomMapping(true);
                for(CustomMapping tempCustomMapping: customMappings) {
                    if (tempCustomMapping.getTarget().equals(tempStep.getFinalStepName())){
                        tempStep.addAnewValOfDDThatConnectedAddToListOFPair(tempCustomMapping.getTargetData(),tempCustomMapping.getSourceData());
                    }
                }
            }
        }
    }
public boolean stepExistInListOFCustomMapping(String nameOfTargetStep){
        for(CustomMapping tempCustomMapping: customMappings){
            if (tempCustomMapping.getTarget().equals(nameOfTargetStep))
                return true;
        }
        return false;
}
    @Override
    public void createFreeInputsForCustomeMapping() {
        List<DataDefinitionDeclaration> tempListInputs = new ArrayList<>();
        int index=1;

        for (StepUsageDeclaration currentStep: steps) {//run on all steps

            System.out.println("---------------------------------------------------------------");
            System.out.println(index+"."+currentStep.getFinalStepName());
            List<DataDefinitionDeclaration> tempInput = currentStep.getStepDefinition().inputs();
            for(DataDefinitionDeclaration DD:tempInput) {

                if (!valueExistsInListAndConnected(tempListInputs,DD,currentStep)){
                    System.out.println(DD.getNameAfterChange());
                    freeInputs.add(new Pair<>(currentStep.getFinalStepName(),DD));
                }
            }
            List<DataDefinitionDeclaration> tempOutput = currentStep.getStepDefinition().outputs();
            System.out.println(currentStep.getStepDefinition().outputs().size());
            for (DataDefinitionDeclaration DDOut:tempOutput) {

                String tempName = existInCustomDD(currentStep,DDOut.getName());
                if (tempName != null){
                    DDOut.setNameForAlias(tempName);
                    tempListInputs.add(DDOut);
                }
                else {
                    tempListInputs.add(DDOut);
                }
                System.out.println(DDOut.getNameAfterChange());
            }
            index++;
        }
    }
    public String existInCustomDD(StepUsageDeclaration step , String name){


       // List<Pair<String,String>> tempList=step.getListOfCustomMapping();
        //for (Pair<String,String> runnerOnPair : tempList){
           // if(runnerOnPair.getKey().equals(name)){
             //   return runnerOnPair.getValue();
            //}
        //}
        //return mappingFromNameToAliasDD.getValByKey(name);
        return step.getFlowLevelAliasInStep(name);
        //return  mappingFromNameToAliasDD.getValueByKeyWithObject((StepUsageDeclarationImpl) step,name);
    }
    public boolean valueExistsInListAndConnected(List<DataDefinitionDeclaration> myList, DataDefinitionDeclaration valueToFind,StepUsageDeclaration step){
        if (existInCustom(myList,valueToFind.getName(),step)){
            //true
            //String newValToFind = getNewValToFind(valueToFind.getName(),step);

           // if (existInCustom(myList,valueToFind,step)){
                return true;
            //}
            //for (DataDefinitionDeclaration runner : myList){
              //  if (runner.getName().equals(newValToFind))
                //    return true;
            //}
        }
        else {
            for (DataDefinitionDeclaration obj : myList) {
                if (mappingFromNameToAliasDD.getValByKey(obj.getName()) == null) {
                    if (obj.getName().equals(valueToFind.getName()) == true)
                        return true;
                } else if (mappingFromNameToAliasDD.getValByKey(obj.getName()).equals(valueToFind.getName()) || obj.getName().equals(valueToFind.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
    public String getNewValToFind(String name,StepUsageDeclaration step){
        List<Pair<String,String>> tempListOfName = step.getListOfCustomMapping();
        for (Pair<String,String> runner: tempListOfName){
            if (runner.getKey().equals(name))
                return runner.getValue();
        }
        return null;//dangerous
    }
    public boolean existInCustom(List<DataDefinitionDeclaration> myList, String valueToFind,StepUsageDeclaration step){
        return step.thisNameOfValExistInTheListOfPair(valueToFind);
    }
    public void createFlowFreeInputs() {
        List<DataDefinitionDeclaration> tempListInputs = new ArrayList<>();
        for (StepUsageDeclaration currentStep: steps) {//run on all steps
            System.out.println(currentStep.getFinalStepName());
                List<DataDefinitionDeclaration> tempInput = currentStep.getStepDefinition().inputs();
                for(DataDefinitionDeclaration DD:tempInput) {

                    if (!valueExistsInList(tempListInputs,DD)){
                        System.out.println(DD.getName());
                        freeInputs.add(new Pair<>(currentStep.getFinalStepName(),DD));
                    }
                }
                List<DataDefinitionDeclaration> tempOutput = currentStep.getStepDefinition().outputs();
                System.out.println(currentStep.getStepDefinition().outputs().size());
                for (DataDefinitionDeclaration DDOut:tempOutput) {
                    System.out.println(DDOut.getName());
                    tempListInputs.add(DDOut);
                }
            }
        }
    public boolean valueExistsInList(List<DataDefinitionDeclaration> myList, DataDefinitionDeclaration valueToFind) {
        for (DataDefinitionDeclaration obj : myList) {
            if (mappingFromNameToAliasDD.getValByKey(obj.getName()) == null){
                if (obj.getName().equals(valueToFind.getName()) == true)
                    return true;
            }
            else if ( mappingFromNameToAliasDD.getValByKey(obj.getName()).equals(valueToFind.getName()) || obj.getName().equals(valueToFind.getName())){
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
                    pairOfStringAndDD.getValue().getNameAfterChange() + " The Necessity is " + pairOfStringAndDD.getValue().necessity()
                    + " Please enter a " + pairOfStringAndDD.getValue().dataDefinition().getName());

            if (pairOfStringAndDD.getValue().getName() == "LINE"){
                int num = myScanner.nextInt();
                context.storeDataValue(pairOfStringAndDD.getValue().getNameAfterChange(),num);
            }else {
                dataToStore = myScanner.nextLine();
                if (!dataToStore.isEmpty()) {
                    context.storeDataValue(pairOfStringAndDD.getValue().getNameAfterChange(),dataToStore);
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
}
