package modules.flow.execution.context;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.StepUsageDeclarationImpl;
import modules.step.api.DataDefinitionDeclaration;

import java.util.*;

public class StepExecutionContextImpl implements StepExecutionContext {
    private final Map<String, Object> dataValues;
    private Map<String,List<String>> log;
    private Map<String,String> summaryLine;
    private StepUsageDeclarationImpl currentStep;
    public StepExecutionContextImpl() {
        dataValues = new HashMap<>();
        summaryLine = new HashMap<>();
        log = new HashMap<>();
    }
    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {

        //todo - check if dataName exists in the context
        // assuming that from the data name we can get to its data definition

        DataDefinitionDeclaration theExpectedDataDefinition = null;

        Optional<DataDefinitionDeclaration> expectedDD = currentStep.getStepDefinition()
                .inputs()
                .stream()
                .filter((input)->input.dataDefinition()
                        .getName() == dataName)
                .findFirst();

        if (expectedDD.isPresent()){
            theExpectedDataDefinition = expectedDD.get();
            if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.dataDefinition().getType())) {
                Object aValue = dataValues.get(dataName);

                return expectedDataType.cast(aValue);

            } else {

                //todo - error handling of some sort...
            }
        }
        else {
            //todo - Error handling
        }
        return null;

    }

    @Override
    public boolean storeDataValue(String dataName, Object value) {
        // assuming that from the data name we can get to its data definition
        DataDefinition theData = null;//todo here needs to compare between the data name

        //todo  we have the DD type so we can make sure that its from the same type we are expecting
        if (theData.getType().isAssignableFrom(value.getClass())) {
            dataValues.put(dataName, value);
        }
        else {
            //todo - error handling of some sort...
        }
        return false;
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
}
