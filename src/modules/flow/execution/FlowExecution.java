package modules.flow.execution;

import modules.flow.definition.api.FlowDefinition;

import java.time.Duration;

public class FlowExecution {//This class accumulates all the data for the flow

    static int idCounter = 1000;

    private final int uniqueId;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;
    //todo need to add extra information we would like to have about flow execution


    public FlowExecution( FlowDefinition flowDefinition) {

        // Here, I am referring to a specific flow for tracing.
        this.uniqueId = (idCounter++);
        this.flowDefinition = flowDefinition;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    public FlowExecutionResult getFlowExecutionResult() {
        return flowExecutionResult;
    }
}
