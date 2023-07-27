package app.body.userManagement;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import app.body.bodyController;
import app.body.bodyInterfaces.bodyControllerDefinition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
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

    private List<StepperUser> currentUsers=new ArrayList<>();
    private Gson gson=new Gson();
    private StepperUser currentStepperUser=null;
    @FXML
    private HBox forManagerSwitch;
    private ToggleSwitch isManager;
    String style=null;
    private bodyController bodyController;
    ToggleGroup group = new ToggleGroup();

    Timer timer =null;
    @FXML
    void initialize() {
        asserts();
        getUsers();
        setSave();
        setTimerUpdater();



    }

    private void setTimerUpdater() {
        try {
            timer.stop();
        }catch (Exception e){
            //do nothing
        }
        timer = new Timer(1500, e -> {
            setUpdater();
        });
        timer.start();

    }
    private void setUpdater() {
        //send req if the users has changed if so to update the list
        Request request = new Request.Builder()
                .url(Constants.USERS_LIST_UPDATE)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to update user roles");
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    //userlist empty

                        if(usersList.getItems().size()>0)
                            Platform.runLater(() -> {
                            usersList.getItems().clear();
                            });
                    return;
                }
                String res = response.body().string();
                try{
                boolean isUpToDate = gson.fromJson(res, Boolean.class);
                response.close();
                //only if success
                if (response.isSuccessful()) {
                    if (!isUpToDate) {
                        updateUsersList();
                    }
                } else {
                    //do nothing
                }
                }catch (Exception e){
                    //do nothing
                }
            }
        });
    }
    private void setSave() {
        saveChanges.setOnMouseEntered(event -> {
            style=saveChanges.getStyle();
            saveChanges.setStyle(style+"-fx-background-color: #00ebf6;-fx-background-radius: 15;");
        });
        saveChanges.setOnMouseClicked(event -> {
            saveChanges.setStyle(style+"-fx-background-color: #0d7277;-fx-background-radius: 15;");
        });
        saveChanges.setOnMouseExited(event -> {
            saveChanges.setStyle(style);
        });
        saveChanges.setOnAction(event -> {
            List<String> newRoles=roleManagement.getTargetItems();
            RequestBody body = RequestBody.create(gson.toJson(newRoles), MediaType.parse("application/json"));
            String username = currentUser;

            //todo move to servlet dah!
            //todo check if manager in servlet if so cant change roles unless the change is change the manager role:(

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
                    response.close();

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
    private void updateUsersList() {
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
                    if (response.code()!=200){
                        System.out.println("user-list empty");
                        if (usersList.getItems().size()>0)
                            Platform.runLater(() -> {
                                usersList.getItems().clear();
                            });
                        return;
                    }
                    String usersJson = response.body().string();
                    response.close();

                    //only if success

                    if (response.isSuccessful()) {
                        List<StepperUser> users = gson.fromJson(usersJson, new TypeToken<List<StepperUser>>() {
                        }.getType());
                        Platform.runLater(() -> {
                            DeltaUpdate(users);
                            currentUsers=users;
                        });
                    } else {
                        Platform.runLater(() -> {
                            JOptionPane.showMessageDialog(null, "Failed to get users list");
                        });
                    }
                }
            });
        }
    private void DeltaUpdate(List<StepperUser> users) {
        List<String> newUsers = users.stream().map(StepperUser::getUsername).collect(Collectors.toList());
        List<String> oldUsers;
        if (currentUsers!=null){
            oldUsers = currentUsers.stream().map(StepperUser::getUsername).collect(Collectors.toList());
        }else {
            oldUsers = new ArrayList<>();
            currentUsers=new ArrayList<>();
        }
        List<String> delta = newUsers.stream().filter(e -> !oldUsers.contains(e)).collect(Collectors.toList());
        if (delta.size() > 0) {
            //new user added
            //todo add to list
            //save previous selection
            String selected=GetSelectedName(usersList);
            //check if something is selected
            group=new ToggleGroup();
            for (RadioButton button:usersList.getItems()) {
                if (oldUsers.contains(button.getText()))
                    button.setToggleGroup(group);
            }
            //add old users to toggle

            for (String user : delta) {
                RadioButton button = new RadioButton(user);
                button.getStyleClass().add("flowRadioButton");
                button.setPrefWidth(250);
                button.setToggleGroup(group);
                String buttonStyle = button.getStyle();
                if(usersList.getItems().size()>0)
                    buttonStyle=usersList.getItems().get(0).getStyle();
                String finalButtonStyle = buttonStyle;
                button.setOnAction(event -> {
                            updateAccordingToUserExe(button.getText());
                            updateAccordingToUserDef(button.getText());
                            currentUser = button.getText();
                            StepperUser stepperUser = users.stream().filter(e -> e.getUsername().equals(button.getText())).findFirst().get();
                            currentStepperUser = stepperUser;
                            setIsManager(stepperUser);
                            button.setStyle(finalButtonStyle);
                        });
                Platform.runLater(() -> {
                    usersList.getItems().add(button);
                });
            }
            if (selected!=null)
                usersList.getItems().stream().filter(e -> e.getText().equals(selected)).findFirst().ifPresent(e -> usersList.getSelectionModel().select(e));

        } else {
            oldUsers.stream().filter(e -> !newUsers.contains(e)).forEach(e -> {
                //user deleted
                usersList.getItems().removeIf(r -> r.getText().equals(e));
            });
        }
    }
    private String GetSelectedName(ListView<RadioButton> usersList) {
        RadioButton selected = usersList.getItems().stream().filter(e -> e.isSelected()).findFirst().orElse(null);
        if (selected != null) {
            return selected.getText();
        }
        return null;
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
                    Platform.runLater(() -> {
                        updateUserList(users);
                    });
                } else {
                    Platform.runLater(() -> {
                        JOptionPane.showMessageDialog(null, "Failed to get users list");
                    });
                }
                response.close();
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
                response.close();
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
        HttpClientUtil.runAsync(request, new Callback() {
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
                response.close();

                RequestBody body = RequestBody.create(gson.toJson(roles), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url(Constants.GET_FLOWS_FOR_ROLE)
                        .post(body)
                        .build();
                HttpClientUtil.runAsync(request, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() ->
                                System.out.println("Something went wrong: " + e.getMessage())

                        );
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() != 200) {

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

        HttpClientUtil.runAsync(request, new Callback() {
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
                if (flowsList.getItems().contains(flow.getName()))
                    continue;
                else{
                    flowsList.getItems().add(flow.getName());
                }
            }
        }

    }//todo all need to be replaced with StepperUser
    private void updateUserList(List<StepperUser> users) {
        Toggle picked=group.getSelectedToggle();
        group.getToggles().clear();
        group=new ToggleGroup();
        usersList.getItems().clear();
        if (users==null)
            return;
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
            try{
                group.selectToggle(picked);
            }catch (Exception e){
                System.out.println("no toggle selected ,probably first time or updated list");
            }

        }
    }
    private void setIsManager(StepperUser user) {
        String username=user.getUsername();
        Request request = new Request.Builder()
                .url(Constants.GET_USER)
                .addHeader("username", username)
                .build();
        HttpClientUtil.runAsync(request, new Callback() {
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
                HttpClientUtil.runAsync(request, new Callback() {
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
                    add.setStyle(add.getStyle() + "-fx-text-fill: #ff1c1c;-fx-font-size: 12;");
                    executionsList.getItems().add(add);
                } else {
                    add.setStyle(add.getStyle() + "-fx-text-fill: #2cff2c;-fx-font-size: 12;");
                    executionsList.getItems().add(add);
                }
            }
        }
    }
    @Override
    public void onLeave() {
        if (timer!=null){
            timer.stop();
        }
    }
    @Override
    public void show() {
        initialize();
    }

    @Override
    public void setBodyController(bodyController body) {
        this.bodyController=body;
    }
    @Override
    public void setFlowsDetails(List<FlowDefinitionDTO> list) {

    }
    @Override
    public void SetCurrentFlow(FlowDefinitionDTO flow) {

    }
}
