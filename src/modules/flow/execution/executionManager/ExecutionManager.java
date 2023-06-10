package modules.flow.execution.executionManager;

import modules.flow.execution.executionManager.tasks.ExecutionTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionManager implements Serializable {
    Integer numberOfThreads;//number of threads in the thread pool
    transient ExecutorService threadExecutor;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // Serialize the necessary data for the ExecutorService, e.g., number of threads
        out.writeInt(numberOfThreads);
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Deserialize the necessary data for the ExecutorService
        numberOfThreads = in.readInt();
        // Re-initialize the ExecutorService
        threadExecutor = Executors.newFixedThreadPool(numberOfThreads);
    }

    public ExecutionManager() {

        numberOfThreads=1;//default
        threadExecutor = Executors.newFixedThreadPool(numberOfThreads);
    }
     ExecutionManager(int numberOfThreads) {

        this.numberOfThreads=numberOfThreads;
        threadExecutor = Executors.newFixedThreadPool(numberOfThreads);

    }
    public void executeTask(ExecutionTask executionTask){
        threadExecutor.execute(executionTask);
    }
    public void setNumberOfThreads(Integer numberOfThreads1){
        this.numberOfThreads = numberOfThreads1;
        threadExecutor=Executors.newFixedThreadPool(numberOfThreads);
    }

    public void shutDown() {
        threadExecutor.shutdown();
    }
}
