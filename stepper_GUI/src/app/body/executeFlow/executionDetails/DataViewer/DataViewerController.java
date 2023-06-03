package app.body.executeFlow.executionDetails.DataViewer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import modules.dataDefinition.impl.enumerator.Enumerator;
import modules.dataDefinition.impl.file.FileData;
import modules.dataDefinition.impl.relation.RelationData;

import java.util.List;

public class DataViewerController {
    @FXML
    private  Label otherLabel;

    @FXML
    private Pane tablePane;

    @FXML
    private Pane listPane;

    @FXML
    private Label name;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label type;
    @FXML
    private Pane otherPane;
    private static Object data;
    private String dataName;
    @FXML
    private Button close;
    @FXML
    void closePopup(ActionEvent event) {
        //close Screen and back to the previous screen
        close.getScene().getWindow().hide();

    }

    @FXML
    void initialize() {
        assert tablePane != null : "fx:id=\"tablePane\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert listPane != null : "fx:id=\"listPane\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert otherPane != null : "fx:id=\"otherPane\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert type != null : "fx:id=\"type\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert otherLabel != null : "fx:id=\"otherLabel\" was not injected: check your FXML file 'DataViewer.fxml'.";

       // init();

    }

    private void init() {
        //set the data name and type
        name.setText(name.getText()+ " : " + dataName);
        double width = mainPane.getPrefWidth();
        name.setPrefWidth(width/1.5);
        //type.setText(type.getText()+ " : " + data.getClass().getSimpleName());
        int state=selectRelevantPane();
        presentData(state);
    }

    private void presentData(int state) {
        switch (state)
        {
            case 1:
                presentOtherData();
                break;
            case 2:
                presentTableData();
                break;
            case 3:
                presentListData();
                break;
        }
    }

    private void presentListData() {

    }

    private void presentTableData() {

    }

    private void presentOtherData() {
        otherLabel.setText(data.toString());
        otherLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;"
                + "-fx-font-family: \"Segoe UI Semibold\";"
                + "-fx-alignment: top-center;");
    }

    private int selectRelevantPane() {//intended to sent real type
        int state = 0;
        if (data instanceof String) {
            state = 1;
            otherPane.setDisable(false);
        }else  if (data instanceof Integer) {
            state = 1;
            otherPane.setDisable(false);
        }else  if (data instanceof RelationData) {
            state = 2;
            tablePane.setDisable(false);
        }else if (data instanceof List) {
            state = 3;
            listPane.setDisable(false);
        } else if (data instanceof Enumerator) {
            state = 1;
            otherPane.setDisable(false);
        } else if (data instanceof FileData) {
            state = 1;
            otherPane.setDisable(false);
        } else {
            state = 1;
            otherPane.setDisable(false);
        }
       return state;
    }

    public void setData(Object value, String name) {
        this.data = value;
        this.dataName = name;
        init();
    }
}
