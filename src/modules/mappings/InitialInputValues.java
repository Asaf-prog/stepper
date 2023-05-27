package modules.mappings;

public class InitialInputValues {
    protected String inputName;

    protected String initialValue;


    public InitialInputValues(String inputName , String initialValue) {
        this.inputName = inputName;
        this.initialValue = initialValue;
    }

    public InitialInputValues() {
        this.inputName = "";
        this.initialValue = "";
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

}
