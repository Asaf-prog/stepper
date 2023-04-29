package modules.flow.execution;

import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;

import java.io.Serializable;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class FlowExecution implements Serializable {//This class accumulates all the data for the flow
    public final UUID uniqueId;
    private final FlowDefinition flowDefinition;
    private final Date startTime;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;
    private Map<String, Object> executionOutputs=new HashMap<>();
    public Map<String, Object> getExecutionOutputs() {
        return executionOutputs;
    }

    public void setExecutionOutputs(Map<String, Object> executionOutputs) {
        this.executionOutputs = executionOutputs;
    }



    //todo  need to add extra information we would like to have about flow execution
    //exceptions!!!
    public FlowExecution(FlowDefinition flowDefinition) {// Here, I am referring to a specific flow for tracing.
        this.uniqueId = GenerateUniqueID();//each flow execution has a unique id
        flowDefinition.addUsage();//for stats
        this.flowDefinition = flowDefinition;
        startTime = new Date();

        //todo get all the info and statistics about the flow execution!!
        //duration, number of steps, number of steps that failed,etc...
    }

    private UUID GenerateUniqueID() {
        return UUID.randomUUID();
    }

    //todo func that update unique id when startup the program!!!
    public Instant startStepTimer() {
        return Instant.now();
    }

    public Instant stopStepTimer() {
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


    public UUID getUniqueId() {
        return uniqueId;
    }

    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    public FlowExecutionResult getFlowExecutionResult() {
        return flowExecutionResult;
    }

    public void setFlowDuration(Duration between) {
        this.totalTime = between;
        this.flowDefinition.updateAvgTime(between);

    }

    public void setFlowExecutionOutputs(StepExecutionContext context) {
        for (String key : flowDefinition.getFlowFormalOutputs()) {
            if (context.getDataValues().containsKey(key)) {
                DataDefinition theExpectedDataType = GetExpectedDataType(key);
                String tempKey=GetOriginalName(key);
                //UpdateContextCurrentStep(context,key);
                Map<String,String> currentString = getCurrentInput(key);
                context.setInputOfCurrentStep(currentString);
              executionOutputs.put(key, context.getDataValue(tempKey, theExpectedDataType.getType()));
            }
        }
    }
    private Map<String,String> getCurrentInput(String key){
        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            List<DataDefinitionDeclaration> outputs = step.getStepDefinition().outputs();
            for (DataDefinitionDeclaration out : outputs) {
                if (step.getByKeyFromOutputMap(out.getName()).equals(key))
                    return step.getOutputFromNameToAlias();
            }
        }
        return null;
    }

    private String GetOriginalName(String key) {
        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            List<DataDefinitionDeclaration> outputs = step.getStepDefinition().outputs();
            for (DataDefinitionDeclaration out : outputs) {
                if (step.getByKeyFromOutputMap(out.getName()).equals(key))
                    return out.getName();
            }
        }
        return null;
    }

    private DataDefinition GetExpectedDataType(String key) {
        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            List<DataDefinitionDeclaration> outputs = step.getStepDefinition().outputs();
            for (DataDefinitionDeclaration out : outputs) {
                if (step.getByKeyFromOutputMap(out.getName()).equals(key))
                    return out.dataDefinition();
            }
        }
        return null;
    }

    public String printOutputs() {
        StringBuilder sb = new StringBuilder();
        for (String key : executionOutputs.keySet()) {
            sb.append(key + " : " + executionOutputs.get(key) + "\n");
        }
        return sb.toString();
    }

    public String getStartDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = sdf.format(startTime);
        return formattedDate;
    }
}



