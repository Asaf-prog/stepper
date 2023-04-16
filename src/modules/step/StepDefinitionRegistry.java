package modules.step;

import modules.step.api.StepDefinition;
import modules.step.impl.HelloWorldStep;
import modules.step.impl.PersonDetailsStep;

public enum StepDefinitionRegistry {
    HELLO_WORLD(new HelloWorldStep()), PERSON_DETAILS(new PersonDetailsStep());
    private final StepDefinition stepDefinition;
    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.stepDefinition = stepDefinition;
    }

    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
}
