package app.body.userManagement;

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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import modules.DataManeger.RoleManager;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.flow.execution.FlowExecution;
import modules.flow.execution.FlowExecutionResult;
import okhttp3.*;
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.control.ToggleSwitch;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.stepper.FlowExecutionDTO;
import util.ClientConstants;
import util.Constants;
import util.http.ClientHttpClientUtil;
import util.http.HttpClientUtil;

import javax.swing.*;

public class UserManagementController implements bodyControllerDefinition {

    @FXML
    private ListView<Label> executionsList;
    @FXML
    private ListView<String> flowsList;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Label just4Style;

    @FXML
    private URL location;

    @FXML
    private Pane userInformationPane;
    String currentUser=null;

    @FXML
    private VBox assignedRolesVbox;
    @FXML
    private HBox hbox4list;
    private ListSelectionView<String> roleManagement;

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
    private VBox infoVbox13;
    @FXML
    private ListView<RadioButton> usersList;

    private List<StepperUser> currentUsers;
    private Gson gson=new Gson();
    private StepperUser currentStepperUser=null;
    @FXML
    private HBox forManagerSwitch;
    private ToggleSwitch isManager;
    @FXML
    void initialize() {
        asserts();
        getUsers();
        setSave();

    }

    private void setSave() {
        saveChanges.setOnAction(event -> {
            List<String> newRoles=roleManagement.getTargetItems();
            RequestBody body = RequestBody.create(gson.toJson(newRoles), MediaType.parse("application/json"));
            String username = currentUser;
            Boolean isManager = this.isManager.isSelected();

            Request request = new Request.Builder()
                    .url(Constants.UPDATE_USER_ROLES)//and isManager
                    .post(body)
                    .addHeader("username",username)
                    .addHeader("isManager",isManager.toString())
                    .build();
            HttpClientUtil.runAsync(request, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("failed to update user roles");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseJson = response.body().string();
                    //only if success
                    if (response.isSuccessful()) {
                        Platform.runLater(() -> {
                            updateAccordingToUserDef(response.header("username"));

                        });
                    } else {
                        Platform.runLater(() -> {
                        });
                    }
                }
            });
        });
    }

    private List<String> getUsers() {
        Request request = new Request.Builder()
                .url(Constants.USERS_LIST)
                .build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to get users list");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String usersJson = response.body().string();
                //only if success

                if (response.isSuccessful()) {
                    List<StepperUser> users = gson.fromJson(usersJson, new TypeToken<List<StepperUser>>() {
                    }.getType());
                    currentUsers=users;
                    updateUserList(users);
                } else {
                    Platform.runLater(() -> {
                        JOptionPane.showMessageDialog(null, "Failed to get users list");
                    });
                }
            }
        });
        return null;
    }
    private List<String> updateUsers() {
        Request request = new Request.Builder()
                .url(Constants.USERS_LIST)
                .build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to get users list");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String usersJson = response.body().string();
                //only if success

                if (response.isSuccessful()) {
                    List<StepperUser> users = gson.fromJson(usersJson, new TypeToken<List<StepperUser>>() {
                    }.getType());
                    currentUsers=users;
                } else {
                    Platform.runLater(() -> {
                        JOptionPane.showMessageDialog(null, "Failed to get users list");
                    });
                }
            }
        });
        return null;
    }


    private void asserts() {
        assert userInformationPane != null : "fx:id=\"userInformationPane\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert assignedRolesVbox != null : "fx:id=\"assignedRolesVbox\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert assignableRolesVbox != null : "fx:id=\"assignableRolesVbox\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert saveChanges != null : "fx:id=\"saveChanges\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert infoVbox1 != null : "fx:id=\"infoVbox1\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert isAdminText != null : "fx:id=\"isAdminText\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert infoVbox11 != null : "fx:id=\"infoVbox11\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert infoVbox12 != null : "fx:id=\"infoVbox12\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert infoVbox13 != null : "fx:id=\"infoVbox13\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert usersList != null : "fx:id=\"usersList\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert executionsList != null : "fx:id=\"executionList\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert flowsList != null : "fx:id=\"flowsList\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert hbox4list != null : "fx:id=\"hbox4list\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert just4Style != null : "fx:id=\"label\" was not injected: check your FXML file 'userManagement.fxml'.";
        assert forManagerSwitch != null : "fx:id=\"forManagerSwitch\" was not injected: check your FXML file 'userManagement.fxml'.";
    }

    private void updateAccordingToUserDef(String name) {
        //todo add to body the roles list asssigned to user
        //getlist of roles for user
        //then update the list of roles and then send to get available flows

        Request request = new Request.Builder()
                .url(Constants.GET_ROLES_FOR_USER)
                .addHeader("username", name)
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
                //do the other req
                List<String> roles = gson.fromJson(response.body().string(), new TypeToken<List<String>>() {
                }.getType());
                updateRolesList(roles);

                RequestBody body = RequestBody.create(gson.toJson(roles), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url(Constants.GET_FLOWS_FOR_ROLE)
                        .post(body)
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
                        if (response.code() != 200) {//because of redirect

                            Platform.runLater(() -> {
                                //present error message
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error44");
                                alert.setContentText("Something went wrong, please try again");
                                alert.showAndWait();
                            });

                            //todo check if stepper valid !!!
                        } else {
                            Platform.runLater(() -> {
                                        try {
                                            handleRespDef(response);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                            );
                        }
                    }
                });


            }
        });
    }

    private void updateRolesList(List<String> roles) {
        Request request = new Request.Builder()
                .url(Constants.GET_ALL_ROLES)
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
                List<String>allRoles=getRolesFromResponse(response);
                if (allRoles==null)
                    return;//if no roles assigned to user
                List<String> assignedRoles=roles;
                List<String> availableRoles=allRoles.stream().filter(role->!assignedRoles.contains(role)).collect(Collectors.toList());//add all roles that are not assigned to user

                ObservableList<String> availableRolesList = FXCollections.observableArrayList(availableRoles);
                ObservableList<String> assignedRolesList = FXCollections.observableArrayList(assignedRoles);
                Platform.runLater(()->{
                    roleManagement=new ListSelectionView<String>();
                    roleManagement.setSourceItems(availableRolesList);
                    roleManagement.setTargetItems(assignedRolesList);
                    roleManagement.setDisable(false);
                    roleManagement.setVisible(true);


                    hbox4list.getChildren().clear();
                    hbox4list.getChildren().add(roleManagement);
                    roleManagement.setPrefWidth(hbox4list.getPrefWidth()-10);
//                    roleManagement.setSourceHeader(label);
//                    roleManagement.setTargetHeader(label2);
                    roleManagement.getStylesheets().add(getClass().getResource("/app/management/style/lists.css").toExternalForm());
                    roleManagement.setStyle("-fx-font-size: 14px; -fx-font-family: 'Segoe UI Semibold'; -fx-pref-width: 900;");

                    // Optionally, you can listen for changes in the selections and update the assignedRoles list accordingly
                    roleManagement.getTargetItems().addListener((ListChangeListener<String>) change -> {
                        assignedRoles.clear();
                        assignedRoles.addAll(roleManagement.getTargetItems());
                    });
                });
            }
        });
    }


    private List<String> getRolesFromResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String body = responseBody.string();
            responseBody.close();
            return gson.fromJson(body, new TypeToken<List<String>>() {
            }.getType());
        }
        return null;
    }


    private void handleRespDef(@NotNull Response response) throws IOException {
        ResponseBody responseBody = response.body();
        flowsList.getItems().clear();
        if (responseBody != null) {
            String body = responseBody.string();
            responseBody.close();
            List<FlowDefinitionDTO> flows = gson.fromJson(body, new TypeToken<List<FlowDefinitionDTO>>() {
            }.getType());
            for (FlowDefinitionDTO flow : flows) {
                flowsList.getItems().add(flow.getName());
            }
        }



    }//todo all need to be replaced with StepperUser

    private void updateUserList(List<StepperUser> users) {
        ToggleGroup group = new ToggleGroup();
        for (StepperUser user : users) {
            RadioButton button = new RadioButton(user.getUsername());
            button.getStyleClass().add("flowRadioButton");
            button.setPrefWidth(250);
            button.setToggleGroup(group);
            button.setOnAction(event -> {
                updateAccordingToUserExe(button.getText());
                updateAccordingToUserDef(button.getText());
                currentUser = button.getText();
                setIsManager(user);
                currentStepperUser=user;
            });
            usersList.getItems().add(button);
            usersList.getStylesheets().add(getClass().getResource("/app/management/style/lists.css").toExternalForm());

        }
    }

    private void setIsManager(StepperUser user) {
        String username=user.getUsername();
        Request request = new Request.Builder()
                .url(Constants.GET_USER)
                .addHeader("username", username)
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
                String body = response.body().string();
                StepperUser user = gson.fromJson(body, StepperUser.class);
                response.body().close();
                //update user in currentusers
                currentUsers.stream().filter(stepperUser -> stepperUser.getUsername().equals(user.getUsername())).forEach(stepperUser -> {
                    stepperUser.setManager(user.getIsManager());
                });
                Platform.runLater(() -> {

                    forManagerSwitch.getChildren().clear();
                    isManager = new ToggleSwitch();
                    isManager.getStylesheets().add(getClass().getResource("/app/management/style/lists.css").toExternalForm());
                    if (user.getIsManager()) {
                        isManager.setSelected(true);
                    } else {
                        isManager.setSelected(false);
                    }
                    forManagerSwitch.getChildren().add(isManager);
                });
            }

        });
    }
            private void updateAccordingToUserExe(String name) {
                //get stepper user by name from servlet and update the view of the roles

                Request request = new Request.Builder()
                        .url(Constants.GET_USER_EXECUTIONS)
                        .addHeader("username", name)
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
                        if (response.code() != 200) {//because of redirect

                            Platform.runLater(() -> {
                                //present error message
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error44");
                                alert.setContentText("Something went wrong, please try again");
                                alert.showAndWait();
                            });
                        } else {
                            Platform.runLater(() -> {
                                        try {
                                            handleRespExe(response);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                            );
                        }
                    }
                });
            }


    private void handleRespExe(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        executionsList.getItems().clear();
        if (responseBody != null) {
            String body = responseBody.string();
            responseBody.close();
            List<FlowExecutionDTO> flows = gson.fromJson(body, new TypeToken<List<FlowExecutionDTO>>() {
            }.getType());
            for (FlowExecutionDTO flow : flows) {
                Label add = new Label(flow.getUniqueId().toString());
                add.getStyleClass().add("list-label");
                //if execution is ended with error, text in red else in green
                if (flow.getFlowExecutionResult().equals(FlowExecutionResult.FAILURE.toString())) {//todo check if works
                    add.setStyle(add.getStyle() + "-fx-text-fill: red;");
                    executionsList.getItems().add(add);
                } else {
                    add.setStyle(add.getStyle() + "-fx-text-fill: green;");
                    executionsList.getItems().add(add);
                }
            }
        }
    }

    @Override
    public void onLeave() {

    }

    @Override
    public void show() {
//        initialize();
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
