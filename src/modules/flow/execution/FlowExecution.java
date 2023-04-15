package modules.flow.execution;

import modules.flow.definition.api.FlowDefinition;

import java.time.Duration;

public class FlowExecution {

    private final String uniqueId;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;
//make sense map from string and relevant value
    //what is the relevant value? and this is why the map will be Map<String,Object>
    // lots more data that needed to be stored while flow is being executed...
    //user input

    public FlowExecution(String uniqueId, FlowDefinition flowDefinition) {
        this.uniqueId = uniqueId;
        this.flowDefinition = flowDefinition;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    public FlowExecutionResult getFlowExecutionResult() {
        return flowExecutionResult;
    }
}
