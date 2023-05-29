package modules.flow.execution.context;
import Menu.MenuException;
import Menu.MenuExceptionItems;
import javafx.util.Pair;
import modules.mappings.CustomMapping;
import modules.mappings.FlowLevelAlias;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;
import modules.step.api.DataDefinitionDeclaration;

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
    public void setUserInputs(FlowExecution flowExecution) throws MenuException {
        List<Pair<String, String>> userInputs = flowExecution.getFlowDefinition().getUserInputs();
        if (userInputs != null) {
            for (Pair<String, String> userInput : userInputs) {
                Class<?> dataType = getDataTypeFromName(userInput.getKey(),flowExecution.getFlowDefinition().getFlowFreeInputs());//get data type by final name
                //validate user input!!
                Object val = casting(userInput.getValue(), dataType);
                //check if the input is the same type as val
                if (val != null && val.getClass().equals(dataType)) {
                    dataValues.put(userInput.getKey(), val);//add to context by final name and input value
                }
                else{
                    if (!userInput.getKey().equals("OPERATION"))//todo remove  this after handling operation
                        throw new MenuException(MenuExceptionItems.EMPTY," User input is not valid for input for " + userInput.getKey() + " with value " + userInput.getValue());
                }
            }
        }
    }
    @Override
    public Map<String, List<Pair<String, String>>> getLogs() {
        return logs;
    }

    @Override
    public  Map<String,String>  getSummaryLines() {
        return summaryLine;
    }

    @Override
    public void setLogs(Map<String, List<Pair<String, String>>> logs) {
        this.logs = logs;
    }
    private Object casting(String value, Class<?> dataType) {
        if (dataType == String.class)
            return value;
        if (dataType == Integer.class)
            return (Integer.parseInt(value));
        if (dataType == Double.class)
            return (Double.parseDouble(value));


        return 0;
    }

    private Class<?> getDataTypeFromName(String key, List<Pair<String, DataDefinitionDeclaration>> flowFreeInputs) {
        for (Pair<String, DataDefinitionDeclaration> temp : flowFreeInputs) {
            if (temp.getKey().equals(key))
                return temp.getValue().dataDefinition().getType();
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
        //store from user input needs to be cast
           String realOutputName = outputOfCurrentStep.get(dataName);
            dataValues.put(realOutputName, value);
        return true;
    }
    @Override
    public List getLog(String step){

        return logs.get(currentWorkingStep.getFinalStepName());
    }
    @Override
    public void setLogsForStep(String step, String log) {
        step = currentWorkingStep.getFinalStepName();
        Date date = new Date();
        if (logs.get(step) == null) {
            List<Pair<String,String>> values = new ArrayList<>();
            Pair<String,String> pair = new Pair<>(log, new SimpleDateFormat("HH:MM:SS.sss").format(date));
            values.add(pair);
            logs.put(step, values);
        } else {
            List<Pair<String,String>> values = logs.get(step);
            Pair<String,String> pair = new Pair<>(log, new SimpleDateFormat("HH:MM:SS.sss").format(date));
            values.add(pair);
            logs.put(step, values);
        }
    }
    @Override
    public void addSummaryLine(String step,String summary) {
        String nameAfterAlias = currentWorkingStep.getFinalStepName();
        summaryLine.put(nameAfterAlias,summary);
    }

    @Override
    public String getSummary(String step) {return summaryLine.get(step);}
   @Override
    public void setStep(StepUsageDeclaration step) { currentWorkingStep = step;}
    public Map<String, Object> getDataValues(){
        return this.dataValues;
    }

}
