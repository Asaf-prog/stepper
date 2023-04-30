package modules.flow.execution.context;
import javafx.util.Pair;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.flow.execution.FlowExecution;
import modules.step.api.DataDefinitionDeclaration;
import modules.step.api.DataDefinitionDeclarationImpl;

import java.text.SimpleDateFormat;
import java.util.*;

public class
StepExecutionContextImpl implements StepExecutionContext {
    private final Map<String, Object> dataValues;
    private  Map <String,String> inputOfCurrentStep;
    private  Map <String,String> outputOfCurrentStep;
    private Map<String,List<Pair<String,String>>> logs;
    private Map<String,String> summaryLine;
    private StepUsageDeclaration currentWorkingStep;
    private List<CustomMapping> customMappings;
    private List<StepUsageDeclaration> steps;

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
    public void initializedCustomMapping(FlowExecution flowExecution) {
        this.customMappings = flowExecution.getFlowDefinition().getCustomMappings();
    }

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
        //get data type from list of all names of flow and cast.

        if (userInputs != null) {
            for (Pair<String, String> userInput : userInputs) {
                Class<?> dataType = GetDataTypeFromName(userInput.getKey());//get data type by final name
                if (dataType == String.class)
                    dataValues.put(userInput.getKey(), userInput.getValue());//add to context by final name and input value
                dataValues.put(userInput.getKey(), userInput.getValue());//add to context by final name and input value
            }
        }
    }

    private int Casting(String value, Class<?> dataType) {
        if (dataType == Integer.class)
            return (Integer.parseInt(value));
        return 0;
    }

    private Class<?> GetDataTypeFromName(String key) {
        for (StepUsageDeclaration step : steps) {
           for (DataDefinitionDeclaration input : step.getStepDefinition().inputs()) {
               if (input.getName().equals(key))
                   return input.dataDefinition().getType();
           }
           for (DataDefinitionDeclaration output : step.getStepDefinition().outputs()) {
               if (output.getName().equals(key))
                   return output.dataDefinition().getType();
           }
        }
        return null;
    }

    @Override
    public <T> T getDataValue(String dataName ,Class<T> expectedDataType) {

            String nameAfterAliasing = inputOfCurrentStep.get(dataName);
            String nameAfterCustomMapping =getCustomMapping(nameAfterAliasing);//check if is on custom mapping and return the source data
            if (nameAfterCustomMapping != null) {  //if after validation
                Optional<Object> aValue = Optional.ofNullable(dataValues.get(nameAfterCustomMapping));
                if (aValue.isPresent())
                    if (expectedDataType.isAssignableFrom(aValue.get().getClass()))
                        return expectedDataType.cast(aValue.get());
            } else {
                Optional<Object> aValue = Optional.ofNullable(dataValues.get(nameAfterAliasing));
                if (aValue.isPresent())
                    if (expectedDataType.isAssignableFrom(aValue.get().getClass()))
                        return expectedDataType.cast(aValue.get());
            }
            return null;
    }
        private String getCustomMapping (String nameAfterAliasing) {//check if is on custom mapping and return the source data name
            for (CustomMapping custom : customMappings) {
                if (currentWorkingStep.getFinalStepName().equals(custom.getTarget())) {
                    if (nameAfterAliasing.equals(custom.getTargetData()))
                        return custom.getSourceData();
                }
            }
            return null;
        }

    @Override
    public boolean storeDataValue(String dataName ,Object value) {
           String realOutputName = outputOfCurrentStep.get(dataName);
            dataValues.put(realOutputName, value);
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
    public Map<String, Object> getDataValues(){
        return this.dataValues;
    }

}
