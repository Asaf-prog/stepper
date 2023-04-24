package modules.flow.execution.context;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.util.*;

public class StepExecutionContextImpl implements StepExecutionContext {
    private final Map<String, Object> dataValues;
    private Map<String,List<String>> log;
    private Map<String,String> summaryLine;
    private StepUsageDeclaration currentWorkingStep;

    private StepUsageDeclarationImpl currentStep;
    public StepExecutionContextImpl() {
        dataValues = new HashMap<>();
        summaryLine = new HashMap<>();
        log = new HashMap<>();
    }
    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {

        //Find of there is an input match
        DataDefinitionDeclaration theExpectedDataDefinition = null;
        Optional<DataDefinitionDeclaration> maybeTheExpectedDataDefinition =
                currentWorkingStep.getStepDefinition()
                        .inputs()
                        .stream()
                        .filter((input) -> input.getName() == dataName)
                        .findFirst();

        if(maybeTheExpectedDataDefinition.isPresent()){
            theExpectedDataDefinition = maybeTheExpectedDataDefinition.get();
            if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.dataDefinition().getType())) {
                Object aValue = dataValues.get(dataName);
                //System.out.println(expectedDataType.cast(aValue).getClass().getName());
                return expectedDataType.cast(aValue);
            }
        }
        else{
            //Handle there is no input;
        }
        return null;
    }

    @Override
    public boolean storeDataValue(String dataName, Object value) {
            dataValues.put(dataName, value);
            return true;
    }
    @Override
    public List getLog(String step){
        return log.get(step);
    }
    @Override
    public void setLog(String step,String logs) {
        if (log.get(step) == null) {
            List<String> values = new ArrayList<>();
            values.add(logs);
            log.put(step, values);

        } else {
            List<String> values = log.get(step);
            values.add(logs);
            log.put(step, values);
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
