package Menu;

public enum MenuExceptionItems {
    INVALID_OPTION("You entered a invalid option"),
    INVALID_INPUT("You entered a invalid input"),
    INVALID_FILE("You entered a invalid file"),
    INVALID_NUMBER_INPUT("You entered a invalid numerical input"),
    INVALID_STRING_INPUT("You entered a invalid string input"),
    EMPTY(""),

    ;

    private final String message;

    MenuExceptionItems(String message) {
        this.message = message;
    };

    public String getMessage() {
        return message;
    }

}
