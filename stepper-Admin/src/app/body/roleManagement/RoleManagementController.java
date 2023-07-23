package app.body.roleManagement;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import app.body.bodyController;
import app.body.bodyInterfaces.bodyControllerDefinition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import modules.DataManeger.Role;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import okhttp3.*;
import org.controlsfx.control.ListSelectionView;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.user.RoleDTO;
import util.Constants;
import util.http.ClientHttpClientUtil;
import util.http.HttpClientUtil;

import javax.swing.*;

public class RoleManagementController implements bodyControllerDefinition{

        @FXML
        private ListView<RadioButton> rolesList;

        @FXML
        private Pane RoleInformationPane;

        @FXML
        private Button saveChanges;

        @FXML
        private ListView<String> UsersList;

        @FXML
        private Label label;

        @FXML
        private HBox hbox4list;

        @FXML
        private Label roleDescription;

        @FXML
        private Button addRole;

        private Gson gson = new Gson();
        private ListSelectionView<String> flowsManagement;
        private String currentRole=null;
        private List<RoleDTO> currentRoles;
        private RoleDTO currentRoleDTO;

    @FXML
        void initialize() {
            assert RoleInformationPane != null : "fx:id=\"RoleInformationPane\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert saveChanges != null : "fx:id=\"saveChanges\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert UsersList != null : "fx:id=\"executionsList\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert hbox4list != null : "fx:id=\"hbox4list\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert roleDescription != null : "fx:id=\"roleDescription\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert addRole != null : "fx:id=\"addRole\" was not injected: check your FXML file 'roleManagement.fxml'.";
            assert rolesList != null : "fx:id=\"rolesList\" was not injected: check your FXML file 'roleManagement.fxml'.";
            updateRoles();
            setSave();

        }

    private void setSave() {
        saveChanges.setOnAction(event -> {
            List<String> newFlows=flowsManagement.getTargetItems();
            RequestBody body = RequestBody.create(gson.toJson(newFlows), MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(Constants.UPDATE_ROLE_FLOWS)
                    .post(body)
                    .addHeader("roleName",currentRole)
                    .build();
            HttpClientUtil.runAsync(request, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("failed to update user roles");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String response1 = response.body().string();
                    RoleDTO updatedRole = gson.fromJson(response1, RoleDTO.class);
                    response.close();

                    //replace with updated role
                    RoleDTO roleDTO = currentRoles.stream().filter(role -> role.getName().equals(updatedRole.getName())).findFirst().get();
                    roleDTO.setFlows(updatedRole.getFlows());//only if success
                    if (response.isSuccessful()) {
                        Platform.runLater(() -> {
                            updateRoles();//todo check if work
                        });
                    } else {
                        Platform.runLater(() -> {
                            JOptionPane.showMessageDialog(null, "Failed to update user roles");
                        });
                    }
                }
            });
        });
    }
    private void updateRoles() {
        Request request = new Request.Builder()
                .url(Constants.GET_ALL_ROLES_DTOS)
                .build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to get roles list");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String usersJson = response.body().string();
                //only if success

                if (response.isSuccessful()) {
                    List<RoleDTO> roles = gson.fromJson(usersJson, new TypeToken<List<RoleDTO>>() {
                    }.getType());
                    currentRoles=roles;
                    Platform.runLater(() -> {
                        updateRolesList();
                    });
                } else {
                    Platform.runLater(() -> {
                        JOptionPane.showMessageDialog(null, "Failed to get roles list");
                    });
                }
            }
        });
    }

    private void updateRolesList() {
        ToggleGroup group = new ToggleGroup();
        for (RoleDTO role : currentRoles) {
            RadioButton button = new RadioButton(role.getName());
            button.getStyleClass().add("flowRadioButton");
            button.setPrefWidth(270);
            button.setToggleGroup(group);
            button.setOnAction(event -> {
                updateUsersList(role);
                updateFlowsList();
                currentRole = button.getText();
                currentRoleDTO=role;
            });
            rolesList.getItems().add(button);
            rolesList.getStylesheets().add(getClass().getResource("/app/management/style/lists.css").toExternalForm());

        }
    }

    private void updateUsersList(RoleDTO role) {
        Request request = new Request.Builder()
                .url(Constants.GET_USERS_FOR_ROLE)
                .addHeader("roleName",role.getName())
                .build();
        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                List<String> users = gson.fromJson(response.body().string(), new TypeToken<List<String>>() {
                }.getType());

                Platform.runLater(() -> {
                    UsersList.getItems().clear();
                    UsersList.getItems().addAll(users);
                    UsersList.getStylesheets().add(getClass().getResource("/app/management/style/lists.css").toExternalForm());
                    UsersList.setCellFactory(param -> new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null || item.isEmpty()) {
                                setText(null);
                            } else {
                                setText(item);
                            }
                        }
                    });
                });

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to get users list");
                e.printStackTrace();
            }
        });
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


    private void updateFlowsList() {
        Request request = new Request.Builder()
                .url(Constants.GET_ALL_FLOWS)
                .build();

        ClientHttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Something went wrong: " + e.getMessage())

                );

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                List<String>allFlows=getFlowsFromResponse(response);
                if (allFlows==null)
                    return;//if no roles assigned to user
                List<String> assignedFlows=new ArrayList<>();
                for (FlowDefinitionDTO flow :currentRoleDTO.getFlows())
                {
                    assignedFlows.add(flow.getName());
                }
                List<String> availableFlows=allFlows.stream().filter(role->!assignedFlows.contains(role)).collect(Collectors.toList());//add all roles that are not assigned to user

                ObservableList<String> availableRolesList = FXCollections.observableArrayList(availableFlows);
                ObservableList<String> assignedRolesList = FXCollections.observableArrayList(assignedFlows);
                Platform.runLater(()->{
                    flowsManagement=new ListSelectionView<String>();
                    flowsManagement.setSourceItems(availableRolesList);
                    flowsManagement.setTargetItems(assignedRolesList);
                    flowsManagement.setDisable(false);
                    flowsManagement.setVisible(true);


                    hbox4list.getChildren().clear();
                    hbox4list.getChildren().add(flowsManagement);
                    flowsManagement.setPrefWidth(hbox4list.getPrefWidth()-10);
//                    roleManagement.setSourceHeader(label);
//                    roleManagement.setTargetHeader(label2);
                    flowsManagement.getStylesheets().add(getClass().getResource("/app/management/style/lists.css").toExternalForm());
                    flowsManagement.setStyle("-fx-font-size: 14px; -fx-font-family: 'Segoe UI Semibold'; -fx-pref-width: 900;");

                    // Optionally, you can listen for changes in the selections and update the assignedRoles list accordingly
                    flowsManagement.getTargetItems().addListener((ListChangeListener<String>) change -> {
                        assignedFlows.clear();
                        assignedFlows.addAll(flowsManagement.getTargetItems());
                    });
                });
            }
        });
    }


    private List<String> getFlowsFromResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String body = responseBody.string();
            responseBody.close();
            return gson.fromJson(body, new TypeToken<List<String>>() {
            }.getType());
        }
        return null;
    }
}
