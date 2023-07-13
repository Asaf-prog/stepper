package app.body.userManagement;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.body.bodyController;
import app.body.bodyInterfaces.bodyControllerDefinition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import modules.DataManeger.users.StepperUser;
import modules.flow.definition.api.FlowDefinitionImpl;
import modules.stepper.Stepper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import util.Constants;
import util.http.HttpClientUtil;

public class UserManagementController implements bodyControllerDefinition {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane userInformationPane;

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
    private VBox infoVbox13;
    @FXML
    private ListView<RadioButton> usersList;
    private Gson gson=new Gson();

    @FXML
    void initialize() {
        asserts();
        //getLastUpdates();


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
    }

    private void getLastUpdates() {
        //go to init admin servlet
        Request request = new Request.Builder()
                .url(Constants.INIT_ADMIN)
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

                    //todo check if stepper valid !!!
                } else {
                    Platform.runLater(() -> {
                                try {
                                    handleResp(response);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                }
            }
        });
    }

    private void handleResp(@NotNull Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String body = responseBody.string();
            List<FlowDefinitionDTO> flows = gson.fromJson(response.body().string(), new TypeToken<List<FlowDefinitionDTO>>() {
            }.getType());
            for (FlowDefinitionDTO flow : flows) {
                System.out.println(flow.getName());

            }
        }



    }//todo all need to be replaced with StepperUser

    private void updateUserList(List<String> users) {
        ToggleGroup group = new ToggleGroup();
        for (String user : users) {
            RadioButton button = new RadioButton(user);
            button.setToggleGroup(group);
            button.setOnAction(event -> {
                updateAccordingToUser(button.getText());
            });
            usersList.getItems().add(button);
        }
    }

    private void updateAccordingToUser(String name) {
        //get stepper user by name from servlet and update the view of the roles

        Request request = new Request.Builder()
                .url(Constants.GET_USER_BY_NAME + name)
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

                    //todo check if stepper valid !!!
                } else {
                    Platform.runLater(() -> {
                                try {
                                    handleUserResp(response);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                }
            }
        });
        }

    private void handleUserResp(Response response) throws IOException {
        //updateLists(response);
        //here he is getting the stepper user from the servlet and updating the view!!!
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String body = responseBody.string();
            List<FlowDefinitionImpl> flows = gson.fromJson(body, new TypeToken<List<FlowDefinitionImpl>>() {
            }.getType());
            for (FlowDefinitionImpl flow : flows) {
                System.out.println(flow.getName());
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
