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

    public FlowExecution(FlowDefinition flowDefinition) {
        // Here, I am referring to a specific flow for tracing.
        //each flow execution has a unique id
        this.uniqueId = GetUniqueID();
        this.flowDefinition = flowDefinition;
        //todo get all the info and statistics about the flow execution!!
        //duration, number of steps, number of steps that failed,etc...
    }

    //todo func that update unique id when startup the program!!!

    private static int GetUniqueID(){
        return ++idCounter;
    }

    public void setFlowExecutionResult(FlowExecutionResult flowExecutionResult) {
        this.flowExecutionResult = flowExecutionResult;
    }

    public void setTotalTime(Duration totalTime) {
        this.totalTime = totalTime;
    }

    public Duration getTotalTime() {
        return totalTime;
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
