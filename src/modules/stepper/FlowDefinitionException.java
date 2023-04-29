package modules.stepper;

public class FlowDefinitionException extends Exception{

    FlowDefinitionExceptionItems exceptionItem;


    public FlowDefinitionException(FlowDefinitionExceptionItems exceptionItem) {
        super(exceptionItem.getMessage());
        this.exceptionItem = exceptionItem;
    }

    public FlowDefinitionExceptionItems getExceptionItem() {
        return exceptionItem;
    }



}
