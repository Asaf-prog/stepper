<<<<<<<< HEAD:stepper_GUI/src/app/StepperApplication.java
package app;
========
package DashboardPage;
>>>>>>>> origin/master:stepper_GUI/src/DashboardPage/Dashboard.java

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Dashboard extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Stepper Application");

<<<<<<<< HEAD:stepper_GUI/src/app/StepperApplication.java
        Parent load = FXMLLoader.load(getClass().getResource("management/app.fxml"));
========
        Parent load = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
>>>>>>>> origin/master:stepper_GUI/src/DashboardPage/Dashboard.java
        Scene scene = new Scene(load,1280,720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);

    }
}
