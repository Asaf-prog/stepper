package ClientsApp.app.body.mainControllerClient;

import ClientsApp.app.body.api.HttpStatusUpdate;
import javafx.beans.property.StringProperty;

import java.io.Closeable;
import java.io.IOException;

public class mainControllerClient implements Closeable, HttpStatusUpdate {
    public mainControllerClient(StringProperty currentUserName) {
        this.currentUserName = currentUserName;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void updateHttpLine(String line) {
       // httpStatusComponentController.addHttpStatusLine(line);
    }
    private final StringProperty currentUserName;
    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }
    public void switchTheLoginPage(){
        //dashboard
    }
}
