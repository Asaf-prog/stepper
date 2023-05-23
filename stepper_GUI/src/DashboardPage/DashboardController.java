package DashboardPage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button historyScreenBtn;

    @FXML
    private AnchorPane paneForXmlLoad;

    @FXML
    private Button dashboardScreenBtn;

    @FXML
    private Button executionScreenBtn;

    @FXML
    private Button statsScreenBtn;

    @FXML
    private Pane sidebarPane;

    @FXML
    private AnchorPane CurrPane;

    @FXML
    private Button buyPremiumBtn;

    @FXML
    private ImageView sidebarIcon;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox sidebarButtons;

    @FXML
    void showDashboardScreen(ActionEvent event) {

    }

    @FXML
    void showExecutionScreen(ActionEvent event) {

    }

    @FXML
    void showStatsScreen(ActionEvent event) {

    }

    @FXML
    void showSideMenu(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert historyScreenBtn != null : "fx:id=\"historyScreenBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert paneForXmlLoad != null : "fx:id=\"paneForXmlLoad\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert dashboardScreenBtn != null : "fx:id=\"dashboardScreenBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert executionScreenBtn != null : "fx:id=\"executionScreenBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert statsScreenBtn != null : "fx:id=\"statsScreenBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert sidebarPane != null : "fx:id=\"sidebarPane\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert CurrPane != null : "fx:id=\"CurrPane\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert buyPremiumBtn != null : "fx:id=\"buyPremiumBtn\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert sidebarIcon != null : "fx:id=\"sidebarIcon\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert sidebarButtons != null : "fx:id=\"sidebarButtons\" was not injected: check your FXML file 'dashboard.fxml'.";
        //set all menu option to unPressable
        dashboardScreenBtn.setDisable(true);
        executionScreenBtn.setDisable(true);
        statsScreenBtn.setDisable(true);
        historyScreenBtn.setDisable(true);


    }
}