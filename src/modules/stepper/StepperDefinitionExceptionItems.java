package modules.stepper;

public enum StepperDefinitionExceptionItems {

    FLOWS_NOT_UNIQUE("Flows are not unique"),
    XML_FILE_NOT_EXIST_OR_EMPTY("Xml file not exist or empty"),


    ;

    private final String message;

    StepperDefinitionExceptionItems(String message) {
        this.message = message;
    };

    public String getMessage() {
        return message;
    }


}