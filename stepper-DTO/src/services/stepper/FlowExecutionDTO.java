package services.stepper;
import com.google.gson.annotations.Expose;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlowExecutionDTO implements Serializable {
    private UUID uniqueId;
    private FlowDefinitionDTO flowDefinition;
    private Map<String, String> summaryLines;
    private Map<String, List<Pair<String, String>>> logs;
    private String startTime;
    private String totalTime;//in ms
    private String flowExecutionResult;
    private Map<String, Object> executionFormalOutputs;//todo will have to change
    private Map<String, Object> allExecutionOutputs;
    private List<Pair<String, String>> userInputs;
    private String executedBy;
    @Expose(deserialize = false)
    private transient SimpleDoubleProperty progress = new SimpleDoubleProperty(0.0);
    @Expose(deserialize = false)
    public transient SimpleBooleanProperty isDone = new SimpleBooleanProperty(false);

    // Constructors, getters, and setters


    public double getProgress() {
        return progress.get();
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public void setIsDone(boolean isDone) {
        this.isDone.set(isDone);
    }

    public SimpleDoubleProperty progressProperty() {
        return progress;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public FlowDefinitionDTO getFlowDefinition() {
        return flowDefinition;
    }

    public void setFlowDefinition(FlowDefinitionDTO flowDefinition) {
        this.flowDefinition = flowDefinition;
    }

    public Map<String, String> getSummaryLines() {
        return summaryLines;
    }

    public void setSummaryLines(Map<String, String> summaryLines) {
        this.summaryLines = summaryLines;
    }

    public Map<String, List<Pair<String, String>>> getLogs() {
        return logs;
    }

    public void setLogs(Map<String, List<Pair<String, String>>> logs) {
        this.logs = logs;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTotalTime() {
        return totalTime;
    }
    @Override
    public boolean equals(Object actualFlowExecutionDTO){//deep compare

        if(actualFlowExecutionDTO instanceof FlowExecutionDTO){
            FlowExecutionDTO compareTo = (FlowExecutionDTO) actualFlowExecutionDTO;
            if(this.uniqueId.equals(compareTo.uniqueId) &&
                    this.flowDefinition.equals(compareTo.flowDefinition) &&
                    this.summaryLines.equals(compareTo.summaryLines) &&
                    this.logs.equals(compareTo.logs) &&
                    this.startTime.equals(compareTo.startTime) &&
                    this.totalTime.equals(compareTo.totalTime) &&
                    this.flowExecutionResult.equals(compareTo.flowExecutionResult) &&
                    this.executionFormalOutputs.equals(compareTo.executionFormalOutputs) &&
                    this.allExecutionOutputs.equals(compareTo.allExecutionOutputs) &&
                    this.userInputs.equals(compareTo.userInputs) &&
                    this.executedBy.equals(compareTo.executedBy)){
                return true;
            }


        }
        return false;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getFlowExecutionResult() {
        return flowExecutionResult;
    }

    public void setFlowExecutionResult(String flowExecutionResult) {
        this.flowExecutionResult = flowExecutionResult;
    }

    public Map<String, Object> getExecutionFormalOutputs() {
        return executionFormalOutputs;
    }

    public void setExecutionFormalOutputs(Map<String, Object> executionFormalOutputs) {
        this.executionFormalOutputs = executionFormalOutputs;
    }

    public Map<String, Object> getAllExecutionOutputs() {
        return allExecutionOutputs;
    }

    public void setAllExecutionOutputs(Map<String, Object> allExecutionOutputs) {
        this.allExecutionOutputs = allExecutionOutputs;
    }

    public List<Pair<String, String>> getUserInputs() {
        return userInputs;
    }

    public void setUserInputs(List<Pair<String, String>> userInputs) {
        this.userInputs = userInputs;
    }

    public void setIsDone(SimpleBooleanProperty done) {
        this.isDone=done;
    }
    public void setValIsDone(boolean done) {
        this.isDone.setValue(done);
    }

    public SimpleBooleanProperty isDoneProperty() {
        return isDone;
    }
}

