package modules.flow.execution.context;
import modules.dataDefinition.api.DataDefinition;
import java.util.HashMap;
import java.util.Map;

public class StepExecutionContextImpl implements StepExecutionContext {

    private final Map<String, Object> dataValues;

    public StepExecutionContextImpl() {
        dataValues = new HashMap<>();
    }

    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {
         //todo - check if dataName exists in the context
        // assuming that from the data name we can get to its data definition
        DataDefinition theExpectedDataDefinition = null;

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
            Object aValue = dataValues.get(dataName);
            //todo - what if it cannot convert?

            return expectedDataType.cast(aValue);
        } else {
            //todo - error handling of some sort...
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
        } else {
            //todo - error handling of some sort...
        }

        return false;
    }
}
