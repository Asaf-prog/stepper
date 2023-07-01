package ClientsApp.app.body.Login;

import ClientsApp.app.body.mainControllerClient.mainControllerClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TextField textFiled;

    @FXML
    private Button loginButton;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private mainControllerClient mainControllerClient;

    @FXML
    void initialize() {
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'loginPage.fxml'.";

    }
    @FXML
    void loginToStepperApplication(ActionEvent event) {
        if (textFiled.getText().isEmpty()){
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions

        String userName = textFiled.getText();
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();
        updateHttpStatusLine("New request is launched for: " + finalUrl);
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.code() != 200) {
//                    String responseBody = response.body().string();
//                    Platform.runLater(() ->
//                            errorMessageProperty.set("Something went wrong: " + responseBody)
//                    );
//                } else {
//                    Platform.runLater(() -> {
//                        mainControllerClient.updateUserName(userName);
//                        mainControllerClient.switchTheLoginPage();
//                    });
//                }

                System.out.println(response.body().string());
            }
        });

    }
    private void updateHttpStatusLine(String data) {
        this.mainControllerClient.updateHttpLine(data);
    }
}
