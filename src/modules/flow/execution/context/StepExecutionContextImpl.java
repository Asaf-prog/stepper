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
        // assuming that from the data name we can get to its data definition
       // DataDefinition theData = null;//todo here needs to compare between the data name

        //todo  we have the DD type so we can make sure that its from the same type we are expecting
       // Class<?> myClass = value.getClass();
        //System.out.println(value.getClass());
      //  System.out.println(theData.getType());
        // System.out.println("asaa");
       // if (DataDefinition.class.isAssignableFrom(myClass)||DataDefinition.class.isInstance(myClass)) {//theData.getType().isAssignableFrom(myClass)
            dataValues.put(dataName, value);
        //}
        //else {
            //todo - error handling of some sort...
        //}
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
