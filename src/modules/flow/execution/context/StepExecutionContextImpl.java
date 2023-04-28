package modules.flow.execution.context;
import javafx.util.Pair;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.flow.execution.FlowExecution;
import modules.step.api.DataDefinitionDeclaration;

import java.text.SimpleDateFormat;
import java.util.*;

public class StepExecutionContextImpl implements StepExecutionContext {
    private final Map<String, Object> dataValues;
    private  Map <String,String> inputOfCurrentStep;
    private  Map <String,String> outputOfCurrentStep;
    private Map<String,List<Pair<String,String>>> logs;
    private Map<String,String> summaryLine;
    private StepUsageDeclaration currentWorkingStep;
    private List<CustomMapping> customMappings;
    private List<StepUsageDeclaration> steps;
    private StepUsageDeclarationImpl currentStep;
    public StepExecutionContextImpl() {
        dataValues = new HashMap<>();
        summaryLine = new HashMap<>();
        logs = new HashMap<>();
        customMappings = new ArrayList<>();
        currentWorkingStep = null;
        inputOfCurrentStep = new HashMap<>();
        outputOfCurrentStep = new HashMap<>();

    }
    @Override
    public void setInputOfCurrentStep(Map <String,String> input){this.inputOfCurrentStep = input;}
    @Override
    public void setOutputOfCurrentStep(Map <String,String> output){this.outputOfCurrentStep = output;}
    @Override
    public void setCustomMappings(List<CustomMapping> customMappings, Map<String,String> mapOfName, List<FlowLevelAlias> FlowLevelAlias){
        this.customMappings = customMappings;
        //set name of steps
        for (StepUsageDeclaration tempStep: steps){
            if (mapOfName.get(tempStep.getFinalStepName()) != null)
                tempStep.setFinalName(mapOfName.get(tempStep.getFinalStepName()));
        }
        }
    @Override
    public void setSteps(List<StepUsageDeclaration> steps){this.steps = steps;}

    @Override
    public void setUserInputs(FlowExecution flowExecution) {
        List<Pair<String, String>> userInputs = flowExecution.getFlowDefinition().getUserInputs();
        if (userInputs != null) {
            for (Pair<String, String> userInput : userInputs) {
                dataValues.put(userInput.getKey(), userInput.getValue());//add to context by final name and input value
            }
        }
    }
        @Override
    public <T> T getDataValue(String dataName ,Class<T> expectedDataType) {






//        for(CustomMapping custome: customMappings){
//            //if target data == source data (type)
//            if(currentWorkingStep.getFinalStepName().equals(custome.getTarget()) && (dataName.equals(custome.getTargetData()))){
//               String targetName = custome.getTargetData();//dd
//               String sourceName = custome.getSourceData();//dd
//                StepUsageDeclaration sourceStep = null;
//               for (StepUsageDeclaration sur :steps){
//                 if (sur.getFinalStepName().equals(custome.getSource())){
//                     sourceStep = sur;
//                 }
//               }
//                DataDefinition source = sourceStep.getStepDefinition().getDataDefinitionByName(custome.getSourceData());
//              // String nameToSearch=sourceStep.getFlowLevelAliasInStep(targetName);
//
//                DataDefinition target = currentWorkingStep.getStepDefinition().getDataDefinitionByNameTarget(custome.getTargetData());
//                if (target.getType() == source.getType()){ //if these steps are same types
//                    Object aValue = dataValues.get(custome.getTargetData());
//                    return expectedDataType.cast(aValue);
//                }
//            }
         Object aValue = dataValues.get(dataName);
         return expectedDataType.cast(aValue);
        }
//        //Find of there is an input match
//        DataDefinitionDeclaration theExpectedDataDefinition = null;
//
//        Optional<DataDefinitionDeclaration> maybeTheExpectedDataDefinition =
//                currentWorkingStep.getStepDefinition()
//                        .inputs()
//                        .stream()
//                        .filter((input) -> input.getName() == dataName)
//                        .findFirst();
//
//        if(maybeTheExpectedDataDefinition.isPresent()){
//            theExpectedDataDefinition = maybeTheExpectedDataDefinition.get();
//            if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.dataDefinition().getType())) {
//                Object aValue = dataValues.get(dataName);
//                //System.out.println(expectedDataType.cast(aValue).getClass().getName());
//                return expectedDataType.cast(aValue);
//            }
//        }
//        else{
//            //Handle there is no input;
//        }
//        return null;
//    }
    @Override
    public boolean storeDataValue(String dataName ,Object value) {
        //auto map
      //  if (currentWorkingStep == null){
            //String finalName=currentWorkingStep.getFlowLevelAliasInStep(dataName);
            dataValues.put(dataName, value);
        //}
        //else {
            //update alias before store into context
             //String finalName=currentWorkingStep.getFlowLevelAliasInStep(dataName);
          //   dataValues.put(dataName, value);
            //}

        //check if there is a custom mapping}
        return true;
    }
    @Override
    public List getLog(String step){
        return logs.get(step);
    }
    @Override
    public void setLogs(String step,String log) {
        Date date = new Date();
        if (logs.get(step) == null) {
            List<Pair<String,String>> values = new ArrayList<>();
            Pair<String,String> pair = new Pair<>(log, new SimpleDateFormat("HH:mm:ss.SSS").format(date));
            values.add(pair);
            logs.put(step, values);
        } else {
            List<Pair<String,String>> values = logs.get(step);
            Pair<String,String> pair = new Pair<>(log, new SimpleDateFormat("HH:mm:ss.SSS").format(date));
            values.add(pair);
            logs.put(step, values);
        }
    }
    @Override
    public void addSummaryLine(String step,String summary) {
        summaryLine.put(step,summary);
    }

    @Override
    public String getSummary(String step) {return summaryLine.get(step);}
   @Override
    public void setStep(StepUsageDeclaration step) { currentWorkingStep = step;}

}
