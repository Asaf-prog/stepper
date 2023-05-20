package DashboardPage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DashboardController {
    @FXML
    private SplitPane dashboardSplitPane;
    @FXML
    private Pane sidebarPane;

    @FXML
    private ImageView sidebarIcon;

    @FXML
    private VBox sidebarButtons;
    @FXML
    private VBox sidebarIcons;

    @FXML
    void handleMouseEntered(ActionEvent event) {
        double newWidth = dashboardSplitPane.getWidth() - sidebarButtons.getWidth();
        dashboardSplitPane.setLayoutX(newWidth);
        sidebarButtons.setVisible(true);
        sidebarButtons.setManaged(true);

    }

    @FXML
    void initialize() {

        assert sidebarIcon != null : "fx:id=\"sidebarIcon\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert sidebarButtons != null : "fx:id=\"sidebarButtons\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert sidebarIcons != null : "fx:id=\"sidebarIcons\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert dashboardSplitPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'dashboard.fxml'.";
        assert sidebarPane != null : "fx:id=\"sidebarPane\" was not injected: check your FXML file 'dashboard.fxml'.";
        sidebarPane.setVisible(false);
        FadeTransition transition = new FadeTransition(Duration.seconds(0.5), sidebarPane);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
        sidebarIcon.setOnMouseClicked(ShowSideBar());

    }

    private EventHandler<? super MouseEvent> ShowSideBar() {
        return event -> {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), dashboardSplitPane);
        transition.setToX(dashboardSplitPane.getLayoutX() - sidebarButtons.getWidth());
        transition.play();
        sidebarPane.setTranslateX(-176);
        transition.setOnFinished((e) -> {
            sidebarPane.setVisible(true);
            sidebarPane.setManaged(true);
            TranslateTransition sideBarTransition = new TranslateTransition(Duration.seconds(0.5), sidebarPane);
            sideBarTransition.setToX(0);
            sideBarTransition.play();
        });
        };
    }
}