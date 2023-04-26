package modules.flow.definition.api;

import javafx.util.Pair;
import modules.step.api.StepDefinition;

import java.time.Duration;
import java.util.List;

public interface StepUsageDeclaration {
    String getFinalStepName();
    StepDefinition getStepDefinition();
    boolean skipIfFail();
    double getAvgTime();

    double updateAvgTime(Duration time);

    int getTimeUsed();
    void isCustomMapping(boolean bool);
    void addUsage();
     boolean getIsCustomMapping();
     void addAnewValOfDDThatConnectedAddToListOFPair(String target,String source);
     void addNewValToPairOFName(String myNameDD,String conectedDD);
     boolean thisNameOfValExistInTheListOfPair(String valueToFind);
    List<Pair<String,String>> getListOfCustomMapping();
    String getName();
     void setFinalName(String name);
}
