package modules.flow.execution.context;

import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.definition.api.StepUsageDeclaration;

import java.util.List;
import java.util.Map;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> expectedDataType);
    boolean storeDataValue(String dataName , Object value);
    List getLog(String step);
    void setLog(String step,String log);
    void addSummaryLine(String step,String summary);
    String getSummary(String step);
    public void setStep(StepUsageDeclaration step);
    void setCustomMappings(List<CustomMapping> customMappings,Map<String,String> mapOfName, List<FlowLevelAlias> FlowLevelAlias);
    void setSteps(List<StepUsageDeclaration> steps);


    // some more utility methods:
    // allow step to store log lines
    // allow steps to declare their summary line
}
