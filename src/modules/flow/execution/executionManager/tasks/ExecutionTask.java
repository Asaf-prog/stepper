package modules.flow.execution.executionManager.tasks;

import modules.flow.execution.FlowExecution;
import modules.flow.execution.runner.FLowExecutor;

import java.sql.SQLOutput;

public class ExecutionTask implements Runnable{
    private String name;
    private FlowExecution flowExecution;
    private FLowExecutor fLowExecutor;

    public ExecutionTask(String name, FlowExecution flowExecution, FLowExecutor fLowExecutor) {
        this.name = name;
        this.flowExecution = flowExecution;
        this.fLowExecutor = fLowExecutor;
    }
    @Override
    public void run() {
        try {
            fLowExecutor.executeFlow(flowExecution);
        } catch (Exception e) {
            System.out.println("Error executing flow: " + flowExecution.getFlowDefinition().getName() + "\n" + e.getMessage());
        }
    }
}
