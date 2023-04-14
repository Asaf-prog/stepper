package modules.flow.execution.context;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> expectedDataType);
    boolean storeDataValue(String dataName, Object value);

    // some more utility methods:
    // allow step to store log lines
    // allow steps to declare their summary line
}
