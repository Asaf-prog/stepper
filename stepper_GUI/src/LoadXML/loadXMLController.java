package LoadXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import modules.DataManeger.GetDataFromXML;

import java.io.File;


public class loadXMLController {

    @FXML
    private Button loadedPath;

    @FXML
    private TextField userPath;

    @FXML
    void loadFileButton(ActionEvent event) throws Exception {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            GetDataFromXML.fromXmlFileToObject(selectedFile.getAbsolutePath());
        }
    }
}
