package ClientsApp.app.body.login;

import ClientsApp.app.body.bodyController;
import ClientsApp.app.body.bodyInterfaces.bodyControllerForLogin;
import ClientsApp.app.body.mainControllerClient.mainControllerClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import modules.flow.definition.api.FlowDefinitionImpl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.ClientConstants;
import util.http.ClientHttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements bodyControllerForLogin {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField textFiled;

    @FXML
    private Button loginButton;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private mainControllerClient mainControllerClient ;
    bodyController body;

    @FXML
    void initialize() {
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'loginPage.fxml'.";
        loginButton.setOnAction(this::loginToStepperApplication);

    }
    @FXML
    void loginToStepperApplication(ActionEvent event) {
        if (textFiled.getText().isEmpty()){
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        String userName = textFiled.getText();
        String finalUrl = HttpUrl
                .parse(ClientConstants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();
        ClientHttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                boolean theAdminExistInSystem = false;
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                    response.close();
                } else {
                    // need to take the list of the flowsName that's back from the Admin
                    List<FlowDefinitionImpl>flows = null;
                    Platform.runLater(() -> {
                        body.getMain().getHeaderComponentController().setVisibleInformation();
                        try {
                            body.switchBodyScreen();
                            body.setNameOnScreen(userName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        //after we get the permission that we can add a new user to our system we crete the client object
                        body.getMain().getHeaderComponentController().setClient(userName,theAdminExistInSystem);
                        body.setClient(body.getMain().getHeaderComponentController().getClient());//set the data-member client in body class from header
                    });

                }
            }
        });
    }
    @Override
    public void show() {
        initialize();
    }
    @Override
    public void setBodyController(bodyController body){
        this.body = body;
    }
}
