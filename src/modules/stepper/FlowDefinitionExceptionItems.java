package modules.stepper;

public enum FlowDefinitionExceptionItems {
    FLOW_STRUCTURE_NOT_VALID("Flow Structure is not valid!!!"),//maybe add num/name of flow
    NO_FLOWS("No Flows in Stepper!!!"),
    FLOW_WITHOUT_STEPS("Flow has no steps!!!"),
    FLOW_WITHOUT_NAME("Flow has no name!!!"),
    FLOW_HAS_DUPLICATE_OUTPUTS("Flow has duplicate outputs!!!"),
    FLOW_HAS_MANDATORY_INPUTS_THAT_ARE_NOT_USER_FRIENDLY("Flow has mandatory inputs that are not user friendly!!!"),
    FLOW_USED_STEP_IN_CUSTOM_MAPPING_THAT_ARE_NOT_EXIST("Flow used step in custom mapping that are not exist!!!"),
    DEFINE_CUSTOM_MAPPING_FOR_DATA_THAT_NOT_EXIST_IN_FLOW("Define custom mapping for data that not exist in flow!!!"),
    DEFINE_ALIAS_FOR_DATA_OR_STEP_THAT_NOT_EXIST_IN_FLOW("Define alias for data or step that not exist in flow!!!"),
    THIS_IS_NOT_THE_FORMAL_OUTPUT("This is not the formal output"),
    FLOW_HAS_STEP_THAT_DOES_NOT_EXIST("Flow has step that does not exist, the step name is: "),
    STEP_IN_ALIAS_NOT_FOUND("Step in alias not found, the step name is: "),
    MORE_THEN_ONE_ALIAS_WITH_THE_SAME_NAME("More then one alias with the same name, the alias name is: "),
    CANNOT_CONNECT_TWO_INPUTS_WITH_DIF_TYPES("Cannot connect two inputs with different types."),
    THIS_FLOW_FOR_CONTINUATION_DOES_NOT_EXIST("this flow for continuation does not exist."),
    THE_INIT_VALUE_ARE_NOT_EXIST("the init value are not exist."),
    THE_TARGET_AND_THE_SOURCE_ARE_NOT_IN_THE_SAME_TYPE("the target and the source are not in the same type."),
    TO_OPERATION_DATA_DEFINITION_YOU_MUST_FILL_ZIP_OR_UNZIP("to operation data definition you must fill zip or unzip")
    ;

    private final String message;
     FlowDefinitionExceptionItems(String message) {
        this.message = message;
    };

    public String getMessage() {
        return message;
    }



}
