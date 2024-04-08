package modules.flow.execution;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;
import modules.dataDefinition.api.DataDefinition;
import modules.flow.definition.api.FlowDefinition;
import modules.flow.definition.api.StepUsageDeclaration;
import modules.flow.execution.context.StepExecutionContext;
import modules.step.api.DataDefinitionDeclaration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class FlowExecution implements Serializable {//This class accumulates all the data for the flow
    public final UUID uniqueId;

    private final FlowDefinition flowDefinition;
    private Map<String, String> summaryLines;
    private Map<String, List<Pair<String, String>>> logs;
    private final Date startTime;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;
    private Map<String, Object> executionFormalOutputs = new HashMap<>();
    private Map<String, Object> allExecutionOutputs = new HashMap<>();
    protected List<Pair<String,String>> userInputs;
    private String executedBy = null;
    public transient SimpleBooleanProperty isDone = new SimpleBooleanProperty(false);

    public transient SimpleDoubleProperty progress=new SimpleDoubleProperty(0.0);

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeBoolean(isDone.get());
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        isDone = new SimpleBooleanProperty(in.readBoolean());
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public double getProgress() {
        return progress.get();
    }
    public SimpleDoubleProperty progressProperty() {
        return progress;
    }

    public List<Pair<String, String>> getUserInputs() {
        return userInputs;
    }
    public void setUserInputs() {
        this.userInputs = flowDefinition.getUserInputs();
        clearUserInputs();
    }
    public void clearUserInputs() {
        this.flowDefinition.clearUserInputs();
    }



    public Map<String, Object> getAllExecutionOutputs() {
        return allExecutionOutputs;
    }

    public void setAllExecutionOutputs(Map<String, Object> allExecutionOutputs) {
        this.allExecutionOutputs = allExecutionOutputs;
    }


    public Map<String, Object> getExecutionOutputs() {
        return executionFormalOutputs;
    }

    public void setExecutionOutputs(Map<String, Object> executionOutputs) {
        this.executionFormalOutputs = executionOutputs;
    }

    public FlowExecution(FlowDefinition flowDefinition) {// Here, I am referring to a specific flow for tracing.
        this.uniqueId = GenerateUniqueID();//each flow execution has a unique id
        flowDefinition.addUsage();//for stats
        this.flowDefinition = flowDefinition;
        startTime = new Date();

        ///duration, number of steps, number of steps that failed,etc...
    }
    public String getOwner() {
        return executedBy;
    }
    public FlowExecution(FlowDefinition flowDefinition,String username) {
        this.uniqueId = GenerateUniqueID();//each flow execution has a unique id
        flowDefinition.addUsage();//for stats
        this.flowDefinition = flowDefinition;
        startTime = new Date();
        executedBy = username;
        userInputs = flowDefinition.getUserInputs();
        ///duration, number of steps, number of steps that failed,etc...
    }

    private UUID GenerateUniqueID() {
        return UUID.randomUUID();
    }

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
                String tempKey = GetOriginalName(key);
                Map<String, String> currentString = getCurrentInput(key);
                context.setInputOfCurrentStep(currentString);
                executionFormalOutputs.put(key, context.getDataValue(tempKey, theExpectedDataType.getType()));
            }
        }
    }

    private Map<String, String> getCurrentInput(String key) {
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

    public Map<String, List<Pair<String, String>>> getLogs() {
        return logs;
    }

    public void setLogs(Map<String, List<Pair<String, String>>> logs) {
        this.logs = logs;
    }

    public String printOutputs() {
        StringBuilder sb = new StringBuilder();
        for (String key : executionFormalOutputs.keySet()) {
            sb.append(key + " :\n" + executionFormalOutputs.get(key) + "\n");
        }
        return sb.toString();
    }

    public String getStartDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = sdf.format(startTime);
        return formattedDate;
    }

    public void setSummaryLines(Map<String, String> summaryLines) {
        this.summaryLines = summaryLines;
    }

    public Map<String, String> getSummaryLines() {
        return summaryLines;
    }

    public void setAllExecutionOutputs(StepExecutionContext context) {
        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            context.setInputOfCurrentStep(step.getOutputFromNameToAlias());
            for (DataDefinitionDeclaration out : step.getStepDefinition().outputs()) {
                String nameAfter = step.getByKeyFromOutputMap(out.getName());
                if (context.getDataValues().containsKey(nameAfter)) {
                    DataDefinition theExpectedData = GetExpectedDataType(nameAfter);
                    String tempKey = GetOriginalName(nameAfter);

                    allExecutionOutputs.put(nameAfter, context.getDataValue(tempKey, theExpectedData.getType()));
                }
            }
        }
    }

    public void endExecution() {
        isDone.setValue(true);
    }

    public SimpleBooleanProperty isDoneProperty() {
        return isDone;
    }

    public boolean isDone() {
        if (isDone == null)
            return false;
        if (isDone.get() == true)
            return true;
        return false;
    }
}



