package modules.flow.execution;

import modules.flow.definition.api.FlowDefinition;
import modules.stepper.Stepper;

import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlowExecution {//This class accumulates all the data for the flow
    private final int uniqueId;
    private final FlowDefinition flowDefinition;
    private final SimpleDateFormat startTime;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;

    //todo need to add extra information we would like to have about flow execution

    public FlowExecution(FlowDefinition flowDefinition) {
        // Here, I am referring to a specific flow for tracing.
        //each flow execution has a unique id
        this.uniqueId = Stepper.GetUniqueID();
        flowDefinition.addUsage();//for stats
        this.flowDefinition = flowDefinition;
        startTime = new SimpleDateFormat("hh:mm:ss");

        //todo get all the info and statistics about the flow execution!!
        //duration, number of steps, number of steps that failed,etc...
    }

    //todo func that update unique id when startup the program!!!

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
