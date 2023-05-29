package app.body.ExecutionsHistory;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import modules.flow.execution.FlowExecutionResult;

import java.text.DecimalFormat;
import java.util.UUID;

public class FlowExecutionTableItem {
    RadioButton id;
    String name;
    String time;
    String result;

    public FlowExecutionTableItem(RadioButton id, String name, String time, String result) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.result = result;
    }
    public FlowExecutionTableItem(UUID id, String name, double time, FlowExecutionResult result) {
        String idString = id.toString();
        this.id = new RadioButton(idString);
        this.id.getStyleClass().add("radio-button-in-table");
        this.name = name;
        //get only the first 3 digits after the dot
        DecimalFormat decimalFormat = new DecimalFormat("#####.#####");
        String formattedValue = decimalFormat.format(time);
        this.time = ""+formattedValue+" MS";
        if(result==FlowExecutionResult.SUCCESS)
            this.result = "Success";
        else if(result==FlowExecutionResult.WARNING)
            this.result = "Warning";
        else
            this.result = "FAIL";
    }


    public RadioButton getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getResult() {
        return result;
    }

    public void setId(RadioButton id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public void addToToggleGroup(ToggleGroup group) {
        id.getStyleClass().add("radio-button-for-table");
        id.setToggleGroup(group);
        id.setStyle("-fx-text-fill: white;");
    }
}