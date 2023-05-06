package modules.stepper;

public class FlowDefinitionException extends Exception{

    private FlowDefinitionExceptionItems exceptionItem;
    private String additionalData;


    public FlowDefinitionException(FlowDefinitionExceptionItems exceptionItem) {
        super(exceptionItem.getMessage());
        this.exceptionItem = exceptionItem;
    }
    public FlowDefinitionException(FlowDefinitionException e) {
        this(e.getExceptionItem(),e.getAdditionalData());
    }
    public FlowDefinitionException(FlowDefinitionExceptionItems exceptionItem,String additionalData) {
        super(exceptionItem.getMessage());
        this.exceptionItem = exceptionItem;
        this.additionalData = additionalData;
    }
    public FlowDefinitionExceptionItems getExceptionItem() {
        return exceptionItem;
    }
    public String getAdditionalData() {
        return additionalData;
    }
    public String getMessage(){
        return exceptionItem.getMessage()+" "+additionalData;
    }
}
