package modules.flow.execution.executionManager.tasks;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;

import java.io.Serializable;
import java.util.UUID;

public class ExecutionTask implements Runnable , Serializable {
    private String name;
    private UUID id;
    public SimpleBooleanProperty isDone = new SimpleBooleanProperty(false);
    private FlowExecution flowExecution;
    private FLowExecutor fLowExecutor;
    private DoubleProperty progress=new SimpleDoubleProperty(0);


    public ExecutionTask(String name, UUID id,FlowExecution flowExecution, FLowExecutor fLowExecutor) {
        this.name = name;
        this.id = id;
        this.flowExecution = flowExecution;
        this.fLowExecutor = fLowExecutor;
    }
    @Override
    public void run() {
        try {
            fLowExecutor.executeFlow(flowExecution , progress);
            EndTask();//notify that the task is done

        } catch (Exception e) {
            System.out.println("Error executing flow: " + flowExecution.getFlowDefinition().getName() + "\n" + e.getMessage());
        }
    }

    public boolean isDone() {
        return isDone.get();
    }
    public SimpleBooleanProperty isDoneProperty() {
        return isDone;
    }

    public void EndTask() {
        isDone.set(true);
        if (flowExecution!=null)
            flowExecution.endExecution();
    }

    public DoubleProperty getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress.set(progress);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id.toString();
    }
    public String get4DigId() {
        return id.toString().substring(0,4);
    }

    public FlowExecution getFlowExecution() {
        return flowExecution;
    }
}
