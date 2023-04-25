package modules.flow.definition.api;

public enum FlowDefinisionExceptionItems {
    FLOW_STRUCTURE_NOT_VALID("Flow Structure is not valid!!!"),//maybe add num/name of flow
    NO_FLOWS("No Flows in Stepper!!!"),
    FLOW_WITHOUT_STEPS("Flow has no steps!!!"),
    FLOW_WITHOUT_NAME("Flow has no name!!!"),

    ;

    private final String message;

     FlowDefinisionExceptionItems(String message) {
        this.message = message;
    };

}
