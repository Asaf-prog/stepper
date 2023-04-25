package modules.flow.definition.api;

import modules.flow.definition.api.StepUsageDeclaration;
import modules.step.api.StepDefinition;

public class StepUsageDeclarationImpl implements StepUsageDeclaration {

    private final StepDefinition stepDefinition;
    private  boolean skipIfFail;
    private final String stepName;
    private  String stepNameAlias;



    public String getStepName() {
        return stepName;
    }

    public String getStepNameAlias() {
        return stepNameAlias;
    }
    public void setSkipIfFail(boolean skipIfFail) {this.skipIfFail = skipIfFail;}


    public void setStepNameAlias(String stepNameAlias) {this.stepNameAlias = stepNameAlias;}

    public StepUsageDeclarationImpl(StepDefinition stepDefinition) {
        this(stepDefinition, false, stepDefinition.name());
    }
    public StepUsageDeclarationImpl(StepDefinition stepDefinition, String name) {
        this(stepDefinition, false, name);
    }
    public StepUsageDeclarationImpl(StepDefinition stepDefinition, boolean skipIfFail, String stepName ) {
        this.stepDefinition = stepDefinition;
        this.skipIfFail = skipIfFail;
        this.stepName = stepName;
        this.stepNameAlias = stepName;
    }
    @Override
    public String getFinalStepName() {
        return stepName;
    }

    @Override
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
}
