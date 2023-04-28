package modules.flow.definition.api;

import javafx.util.Pair;
import modules.step.api.StepDefinition;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface StepUsageDeclaration {
    String getFinalStepName();

    StepDefinition getStepDefinition();

    boolean skipIfFail();

    double getAvgTime();

    void addAlias(String name, String alias);

    double updateAvgTime(Duration time);

    int getTimeUsed();

    void isCustomMapping(boolean bool);

    void addUsage();

    boolean getIsCustomMapping();

    void setFinalName(String name);

    void addToMapOfInput(String name, String alias);

    void addToMapOfOutput(String name, String alias);

    boolean thisValueExistInTheMapInput(String valToCheck);

    boolean thisValueExistInTheMapOutput(String valToCheck);

    String getByKeyFromInputMap(String key);
    String getByKeyFromOutputMap(String key);
    Map<String,String> getInputFromNameToAlias();
     Map<String,String> getOutputFromNameToAlias();
    Instant startStepTimer();
    Instant stopStepTimer();
    void setStepDuration(Duration duration);
}
