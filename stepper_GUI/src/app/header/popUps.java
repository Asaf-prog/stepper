package app.header;

import javafx.application.Application;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class popUps extends Application {
        @Override
        public void start(Stage primaryStage) {

            // Create the main layout
            StackPane root = new StackPane();
            root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

            // Create the content for the popup
            StackPane content = new StackPane();
            content.setStyle("-fx-background-color: black; -fx-padding: 20;");
            content.setAlignment(Pos.CENTER);
            Text messageText = new Text("                                                                             \n" +
                    "                      *          ,&&&&&,    ./                                  \n" +
                    "                            %#/*//((###%%%%##%%/                                \n" +
                    "                         #/**/(#%%%%%%%%%%%%%%%%###                             \n" +
                    "                   .  %/*,,*(##%%%%%%%%%%%%%%%%%%%#%*                           \n" +
                    "                     #,,,,/(###%%%%%%%%%%%%%%%%%%%%%##*                         \n" +
                    "                    #*,,,*/###((/////(#%%%%%%%%%%%%%%(#*                        \n" +
                    "                 #/*,,,,,*((/,,,,,,,,*/(###(/**/(#%%%(*(*      .,               \n" +
                    "             ,.#*,,,....,//**/**,,,,***,*****,,,,,*##/*,/%        /             \n" +
                    "             %/,.,,,,..,/(#########(***/%#/*,,,,**,*(*,,,/&                     \n" +
                    "            (*,..,,.,.,(##%%%%%%%%%%%%%%%%%%####(((/*,.,,,*%                    \n" +
                    "            (,...,....,((((####%%%%%##%%%%%%%%%%%%%%#*,..,*#.                   \n" +
                    "        ,   %*........,/((((######((##(#####%%%%%%%%%/,...,*#                   \n" +
                    "            %*,...,,.,**/((####((/**,,,*/*/((##%%###(*,,,,*/%                   \n" +
                    "             /,..,......,/((((//*,,.,,,,,,,,*/#####/,,..,*%                     \n" +
                    "             %/,,,**,...,,/((*,..,**//(((/**,,*##(*,.,,.,*(.                    \n" +
                    "               %##&#*.....,,*,.*/(((((((#(/*,,,/*,,..,,,*(       .              \n" +
                    "                  (*,,,....,..,*(((*,,,*/##(/,,,,....,/##(//%                   \n" +
                    "                 ## **/#((/*,,.,,,,....,,//*,,,,...,/#          (               \n" +
                    "                           %//,,...........,**,,.,*%                            \n" +
                    "                         #&%%&&&%%%%%%%%%%%&  .%%%&            ,           ");
            messageText.setFill(Color.BLACK);

            messageText.setFill(Color.WHITE);
            content.getChildren().addAll(messageText);
            root.getChildren().add(content);

            Scene scene = new Scene(root, 1080, 720 );
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

