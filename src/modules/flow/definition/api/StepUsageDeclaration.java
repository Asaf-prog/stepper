package modules.flow.definition.api;

import modules.step.api.StepDefinition;

import java.time.Duration;

public interface StepUsageDeclaration {
    String getFinalStepName();
    StepDefinition getStepDefinition();
    boolean skipIfFail();
    double getAvgTime();

    double updateAvgTime(Duration time);

    int getTimeUsed();

    void addUsage();
}
