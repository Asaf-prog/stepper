package services.stepper.other;

public class InitialInputValuesDTO {
    private String name;
    private String value;

    public InitialInputValuesDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public InitialInputValuesDTO() {
        this.name = "";
        this.value = "";
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
