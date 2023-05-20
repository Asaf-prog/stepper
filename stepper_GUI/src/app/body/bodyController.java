package app.body;

import app.management.mainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import modules.flow.definition.api.FlowDefinitionImpl;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class bodyController implements bodyControllerDefinition{
    private mainController main;
    @FXML
    private AnchorPane bodyPane;
    public void setMainController(mainController main) {
        this.main = main;
    }
    public void showFlowDefinition() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("flowDefinitionPresent.fxml");
        fxmlLoader.setLocation(url);
        loadScreen(fxmlLoader, url);
    }
    private void loadScreen(FXMLLoader fxmlLoader,URL url) {
        try {
            Parent screen = fxmlLoader.load(url.openStream());
            bodyControllerDefinition bController = fxmlLoader.getController();
            bController.setFlowsDetails(main.getFlows());
            bController.show();
          //  bController.setBodyController(this);
            bodyPane.getChildren().setAll(screen);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void show() {
        //create a vBox in scene and create a new button that present the flows names
        //create an instance fo flow visible

    }
    @Override
    public void setBodyController(bodyController body) {

    }
    @Override
    public void setFlowsDetails(List<FlowDefinitionImpl> list) {

    }
}
