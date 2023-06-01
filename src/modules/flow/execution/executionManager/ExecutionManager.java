package modules.flow.execution.executionManager;

import modules.flow.execution.executionManager.tasks.ExecutionTask;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionManager {
    List<ExecutionTask> executionTasks;
    Integer numberOfThreads;//number of threads in the thread pool
//todo dont forget shutdown
    ExecutorService threadExecutor;
    ExecutionManager(List<ExecutionTask> executionTasks, Integer numberOfThreads){
        this.executionTasks = executionTasks;
        this.numberOfThreads = numberOfThreads;
    }
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

    public static void main(String[] args) {
        while(true){
            System.out.println("hi");
        }
    }
}
