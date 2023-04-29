package modules.stepper;

public class StepperDefinitionException extends Exception{
    StepperDefinitionExceptionItems exceptionItem;

    public StepperDefinitionException(StepperDefinitionExceptionItems exceptionItem) {
        super(exceptionItem.getMessage());
        this.exceptionItem = exceptionItem;
    }


    public StepperDefinitionExceptionItems getExceptionItem() {
        return exceptionItem;
    }
}

