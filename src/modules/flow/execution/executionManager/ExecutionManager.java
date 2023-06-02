package modules.flow.execution.executionManager;

import modules.flow.execution.executionManager.tasks.ExecutionTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionManager implements Serializable {
    List<ExecutionTask> executionTasks;
    Integer numberOfThreads;//number of threads in the thread pool
    ExecutorService threadExecutor;

    public ExecutionManager() {
        executionTasks=new ArrayList<>();
        numberOfThreads=4;//todo change !!!
        threadExecutor = Executors.newFixedThreadPool(numberOfThreads);
    }
     ExecutionManager(int numberOfThreads) {
        executionTasks=new ArrayList<>();
        this.numberOfThreads=numberOfThreads;
        threadExecutor = Executors.newFixedThreadPool(numberOfThreads);

    }
    public void addTask(ExecutionTask executionTask){
        executionTasks.add(executionTask);
    }
    public void executeTask(ExecutionTask executionTask){
        threadExecutor.execute(executionTask);
    }
    public void setNumberOfThreads(Integer numberOfThreads){
        this.numberOfThreads = numberOfThreads;
        threadExecutor=Executors.newFixedThreadPool(numberOfThreads);
    }
}
