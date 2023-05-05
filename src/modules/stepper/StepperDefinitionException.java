package modules.stepper;

public class StepperDefinitionException extends Exception{
    StepperDefinitionExceptionItems exceptionItem;

    String additionalData;

    public StepperDefinitionException(StepperDefinitionExceptionItems exceptionItem,String additionalData) {
        super(exceptionItem.getMessage());
        this.exceptionItem = exceptionItem;
        this.additionalData = additionalData;
    }
    public StepperDefinitionException(StepperDefinitionExceptionItems exceptionItem) {
        super(exceptionItem.getMessage());
        this.exceptionItem = exceptionItem;
        this.additionalData =" ";
    }
    public String getMessage(){
        return exceptionItem.getMessage()+" "+additionalData;
    }

    public StepperDefinitionExceptionItems getExceptionItem() {
        return exceptionItem;
    }
}

