package app.body.roleManagement;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.body.bodyController;
import app.body.bodyInterfaces.bodyControllerDefinition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import modules.flow.definition.api.FlowDefinitionImpl;

public class RoleManagementController implements bodyControllerDefinition {
        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Pane RoleInformationPane;

        @FXML
        private VBox assignedRolesVbox;

        @FXML
        private VBox assignableRolesVbox;

        @FXML
        private Button saveChanges;

        @FXML
        private VBox infoVbox1;

        @FXML
        private Label isAdminText;

        @FXML
        private VBox infoVbox11;

        @FXML
        private VBox infoVbox12;

        @FXML
        private Button newRoleButton;

        @FXML
        void initialize() {
            assert RoleInformationPane != null : "fx:id=\"RoleInformationPane\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert assignedRolesVbox != null : "fx:id=\"assignedRolesVbox\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert assignableRolesVbox != null : "fx:id=\"assignableRolesVbox\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert saveChanges != null : "fx:id=\"saveChanges\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert infoVbox1 != null : "fx:id=\"infoVbox1\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert isAdminText != null : "fx:id=\"isAdminText\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert infoVbox11 != null : "fx:id=\"infoVbox11\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert infoVbox12 != null : "fx:id=\"infoVbox12\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert newRoleButton != null : "fx:id=\"newRoleButton\" was not injected: check your FXML file 'roleManagement.fxml'.";

        }

    @Override
    public void onLeave() {

    }

    @Override
    public void show() {
            initialize();


    }

    @Override
    public void setBodyController(bodyController body) {

    }

    @Override
    public void setFlowsDetails(List<FlowDefinitionImpl> list) {

    }

    @Override
    public void SetCurrentFlow(FlowDefinitionImpl flow) {

    }
}
