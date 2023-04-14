package modules.flow.execution;

import modules.flow.definition.api.FlowDefinition;

import java.time.Duration;

public class FlowExecution {

    private final String uniqueId;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;

    // lots more data that needed to be stored while flow is being executed...

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
