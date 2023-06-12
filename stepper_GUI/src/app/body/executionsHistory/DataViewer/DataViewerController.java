package app.body.executionsHistory.DataViewer;
import app.management.style.StyleManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML
    private Button close;
    @FXML
    private ListView<String> list;
    @FXML
    private TableView<RelationData.SingleRow> tableView;
    private static Object data;
    private String dataName;

    @FXML
    void closePopup(ActionEvent event) {
        //close Screen and back to the previous screen
        close.getScene().getWindow().hide();

    }
    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }
    @FXML
    void initialize() {
        setTheme();
        assert tablePane != null : "fx:id=\"tablePane\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert listPane != null : "fx:id=\"listPane\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert otherPane != null : "fx:id=\"otherPane\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert type != null : "fx:id=\"type\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert otherLabel != null : "fx:id=\"otherLabel\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'DataViewer.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'DataViewer.fxml'.";

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
            default://Enum ?
                presentOtherData();
        }
    }

    private void presentListData() {
        list=new ListView<>();
        listPane.setDisable(false);
        listPane.setVisible(true);
        ObservableList<?> listData = FXCollections.observableArrayList((List<?>) data);
       // listData.forEach(System.out::println);
        ObservableList<String> listData2=FXCollections.observableArrayList();
        for (Object o:listData) {//convert to string
            String item =o.toString();
            listData2.add(item);
        }
        if (listData2.size()==0)
            listData2.add("-------------Empty List---------------");

        list.setItems(listData2);

        list.setPrefWidth(listPane.getPrefWidth()-10);
        listPane.getChildren().add(list);
        list.prefWidthProperty().bind(listPane.widthProperty());
        list.prefHeightProperty().bind(listPane.heightProperty());
        list.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;"
                + "-fx-font-family: \"Segoe UI Semibold\";"
                + "-fx-alignment: top-center; -fx-background-color: #36393e; -fx-text-fill: purple;" +
                "-fx-border-color: white; -fx-border-width: 1px;");
        listPane.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;"
                + "-fx-font-family: \"Segoe UI Semibold\";"
                + "-fx-alignment: top-center; -fx-background-color: #36393e; -fx-text-fill: purple;" +
                "-fx-border-color: white; -fx-border-width: 1px;");
    }

    private void presentTableData() {
        tableView= new TableView<>();
        tablePane.setDisable(false);
        tablePane.setVisible(true);
        tableView.setDisable(false);
        tableView.setVisible(true);
        RelationData relationData =(RelationData) data;

        ObservableList<RelationData.SingleRow> rowData = FXCollections.observableArrayList(relationData.getRows());
        if (relationData.isEmpty())
            tablePane.getChildren().add(new Label("-------------Empty Table---------------"));


            for (int i = 0; i < relationData.getNumColumns(); i++) {
            TableColumn<RelationData.SingleRow, String> column = new TableColumn<>(relationData.getValInList(i));
            final int columnIndex = i;
            column.setCellValueFactory(cellData -> {
                String value = cellData.getValue().getData().get(columnIndex);
                return new SimpleStringProperty(value);            });
            tableView.getColumns().add(column);
        }


        tableView.setItems(rowData);
        tableView.setPrefWidth(tablePane.getPrefWidth()-10);
        tableView.prefWidthProperty().bind(tablePane.widthProperty());
        tableView.prefHeightProperty().bind(tablePane.heightProperty());
        tableView.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;"
                + "-fx-font-family: \"Segoe UI Semibold\";"
                + "-fx-alignment: top-center; -fx-background-color: blue; -fx-text-fill: purple;" +
                "-fx-border-color: white; -fx-border-width: 1px;");
       tablePane.getChildren().add(tableView);


    }

    private void presentOtherData() {
        otherLabel.setText(data.toString());
        otherLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;"
                + "-fx-font-family: \"Segoe UI Semibold\";"
                + "-fx-alignment: top-center; -fx-font-size: 18px;");
        otherLabel.setWrapText(true);
        otherLabel.setPrefWidth(mainPane.getPrefWidth()-20);
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
