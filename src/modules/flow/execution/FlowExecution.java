package modules.flow.execution;

import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.time.Instant;
@XmlRootElement(name = "flow-Executions")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlowExecution {//This class accumulates all the data for the flow
    @XmlAttribute
    private final int uniqueId;
    @XmlElement(name = "flow Definition in flow Execution")
    private final FlowDefinition flowDefinition;
    @XmlElement
    private final SimpleDateFormat startTime;
    @XmlElement
    private Duration totalTime;
    @XmlElement
    private FlowExecutionResult flowExecutionResult;
    //todo  need to add extra information we would like to have about flow execution
    //exceptions!!!

    public FlowExecution(){
        uniqueId = 0;
        flowDefinition = null;
        startTime = null;
        totalTime = null;
        flowExecutionResult = null;
    }
    public FlowExecution(FlowDefinition flowDefinition) {// Here, I am referring to a specific flow for tracing.
        this.uniqueId = Stepper.GetUniqueID();//each flow execution has a unique id
        flowDefinition.addUsage();//for stats
        this.flowDefinition = flowDefinition;
        startTime = new SimpleDateFormat("hh:mm:ss");

        //todo get all the info and statistics about the flow execution!!
        //duration, number of steps, number of steps that failed,etc...
    }


    //todo func that update unique id when startup the program!!!
    public Instant startStepTimer(){
        return Instant.now();
    }
    public Instant stopStepTimer(){
        return Instant.now();
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

    public void setFlowDuration(Duration between) {
        this.totalTime=between;
        this.flowDefinition.updateAvgTime(between);

    }
}
