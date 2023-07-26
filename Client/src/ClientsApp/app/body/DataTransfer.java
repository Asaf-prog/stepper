package ClientsApp.app.body;

import javafx.fxml.FXMLLoader;
import services.user.ContinuationConversionDTO;

import java.net.URL;

public class DataTransfer {
    private  ContinuationConversionDTO dataListFromServlet;
    private String flowSourceName;
    private String flowTargetName;
    private FXMLLoader fxmlLoader;
    private URL url;
    public DataTransfer( ContinuationConversionDTO data,String flowSourceName,String flowTargetName){
        this.dataListFromServlet = data;
        this.flowSourceName = flowSourceName;
        this.flowTargetName = flowTargetName;
    }
    public ContinuationConversionDTO getDataListFromServlet(){
        return dataListFromServlet;
    }
    public String getFlowSourceName(){
        return flowSourceName;
    }
    public String getFlowTargetName(){
        return flowTargetName;
    }
    public void setUrl(URL url){
        this.url = url;
    }
    public URL getUrl(){
        return url;
    }
    public void setFxmlLoader(FXMLLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
    }
    public FXMLLoader getFxmlLoader(){
        return fxmlLoader;
    }
}
