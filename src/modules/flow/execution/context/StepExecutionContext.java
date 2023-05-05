package modules.flow.execution.context;

import Menu.MenuException;
import javafx.util.Pair;
import modules.Map.CustomMapping;
import modules.Map.FlowLevelAlias;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.FlowExecution;

import java.util.List;
import java.util.Map;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> expectedDataType);
    boolean storeDataValue(String dataName , Object value);
    List getLog(String step);
    void setLogsForStep(String step, String log);
    void addSummaryLine(String step,String summary);
    String getSummary(String step);
    public void setStep(StepUsageDeclaration step);
    void setCustomMappings(List<CustomMapping> customMappings,Map<String,String> mapOfName, List<FlowLevelAlias> FlowLevelAlias);
    void setSteps(List<StepUsageDeclaration> steps);

    void setUserInputs(FlowExecution flowExecution) throws MenuException;
    void setInputOfCurrentStep(Map <String,String> input);
    void setOutputOfCurrentStep(Map <String,String> output);
    void initializedCustomMapping(FlowExecution flowExecution);
    Map<String, Object> getDataValues();
    void setLogs(Map<String, List<Pair<String, String>>> logs);
    Map<String, List<Pair<String, String>>> getLogs();

    Map<String,String>  getSummaryLines();
}
