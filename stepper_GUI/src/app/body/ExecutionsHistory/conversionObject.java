package app.body.ExecutionsHistory;

import modules.flow.execution.FlowExecutionResult;

import java.time.Duration;

public class conversionObject {
    private String time;
    private String name;
    private String res;

    public conversionObject(String name, String time, String res) {
        this.name = name;
        this.time = time;
        this.res = res;
    }
}
