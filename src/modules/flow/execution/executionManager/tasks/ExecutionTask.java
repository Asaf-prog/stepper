package modules.flow.execution.executionManager.tasks;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;

import java.io.Serializable;

public class ExecutionTask implements Runnable , Serializable {
    private String name;
    private FlowExecution flowExecution;
    private FLowExecutor fLowExecutor;
    private DoubleProperty progress=new SimpleDoubleProperty(0);

    public ExecutionTask(String name, FlowExecution flowExecution, FLowExecutor fLowExecutor) {
        this.name = name;
        this.flowExecution = flowExecution;
        this.fLowExecutor = fLowExecutor;
    }
    @Override
    public void run() {
        try {
            fLowExecutor.executeFlow(flowExecution , progress);
        } catch (Exception e) {
            System.out.println("Error executing flow: " + flowExecution.getFlowDefinition().getName() + "\n" + e.getMessage());
        }
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
}
