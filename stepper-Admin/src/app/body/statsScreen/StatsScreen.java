package app.body.statsScreen;

import app.body.bodyController;
import app.body.bodyInterfaces.bodyControllerDefinition;
import app.management.style.StyleManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import services.stepper.FlowDefinitionDTO;
import services.stepper.flow.StepUsageDeclarationDTO;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class StatsScreen implements bodyControllerDefinition {
    FlowDefinitionDTO currSelectedFlow = null;//event on choosen flow
    StepUsageDeclarationDTO currSelectedStep = null;//event on choosen step
    @FXML
    private ResourceBundle resources;
    @FXML
    private HBox chartsPane;

    @FXML
    private HBox chart2;
    @FXML
    private URL location;

    @FXML
    private ListView<String> flowStatsList;

    @FXML
    private ListView<RadioButton> flowsList;

    private Gson gson = new Gson();

    @FXML
    private Pane flowPane;

    @FXML
    private Label flowDefinitionsSize;

    @FXML
    private  ListView<String> stepsList;

    @FXML
    private ListView<String> stepStatsList;

    @FXML
    private Label flowExecutionsSize;

    @FXML
    private Pane stepperPane;

    private String listStyle;

    private List<FlowDefinitionDTO> updatedFlows;


    @FXML
    void initialize() {
        setTheme();
        listStyle=flowsList.getStyle();
        asserts();
        setListsToVisible();
        //setListsView();
        Binds();
        getLastUpdates();
        //set listeners
        setListeners();
        setListsView();
    }

    private void getLastUpdates() {

        Request request = new Request.Builder()
                .url(Constants.GET_FLOWS_DEF)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed to get all flows");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String json = response.body().string();
                response.close();
                List<FlowDefinitionDTO> flowExecutionDTOS = gson.fromJson(json, new TypeToken<List<FlowDefinitionDTO>>() {
                }.getType());

                updatedFlows = flowExecutionDTOS;
                Platform.runLater(() -> {
                    updateLists();
                });}
        });
    }

    private static void setTheme() {
        StyleManager.setTheme(StyleManager.getCurrentTheme());
    }

    private void asserts() {
        assert flowPane != null : "fx:id=\"flowPane\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowsList != null : "fx:id=\"flowsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert stepsList != null : "fx:id=\"stepsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowDefinitionsSize != null : "fx:id=\"flowDefinitionsSize\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert stepStatsList != null : "fx:id=\"stepStatsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowStatsList != null : "fx:id=\"flowStatsList\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert flowExecutionsSize != null : "fx:id=\"flowExecutionsSize\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert stepperPane != null : "fx:id=\"stepperPane\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert chartsPane != null : "fx:id=\"chartsPane\" was not injected: check your FXML file 'StatsScreen.fxml'.";
        assert chart2 != null : "fx:id=\"chart2\" was not injected: check your FXML file 'StatsScreen.fxml'.";
    }

    private void setCharts(FlowDefinitionDTO selectedFlow) {


        BarChart<String, Number> barChart = getBarChart(selectedFlow);
        PieChart pie= getPieChart(selectedFlow);
        chartsPane.getChildren().clear();
        chart2.getChildren().clear();
        chart2.setAlignment(Pos.BOTTOM_CENTER);
        chart2.getChildren().add(barChart);
        chartsPane.setAlignment(Pos.BOTTOM_CENTER);
        chartsPane.getChildren().add(pie);



    }

    private BarChart<String, Number> getBarChart(FlowDefinitionDTO selectedFlow) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Steps");
        yAxis.setLabel("Time (in ms)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        //barChart.getStylesheets().add("charts.css");
        barChart.setTitle("Per Step Time Taken");
        barChart.setStyle(" -fx-font-size: 16");
        barChart.setMinHeight(250);
        barChart.setMaxHeight(250);

        //barChart.setMaxWidth(350);// Adjust the desired height of the chart

        // Set styling for title and axes labels
        barChart.lookup(".chart-title").setStyle("-fx-text-fill: #d000ff;-fx-font-size: 16");
        barChart.lookup(".axis-label").setStyle("-fx-text-fill: #d100ff; -fx-font-size: 16");

        // Set X-axis label color to white
        barChart.getYAxis().setStyle("-fx-text-fill: #ffffff;-fx-font-size: 16"); // Set Y-axis label color to white

        // Create data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Time Taken");
        xAxis.setTickLabelRotation(90);
        // Add data to the series
        for (StepUsageDeclarationDTO step : selectedFlow.getSteps()) {
            series.getData().add(new XYChart.Data<>(step.getFinalStepName(), step.getAvgTime()));
        }

        // Set styling for the chart plot and legend
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: #24292e;");
        barChart.lookup(".chart-legend").setStyle("-fx-text-fill: #ffffff;");

        // Set styling for the bars
        for (Node node : barChart.lookupAll(".bar")) {
            node.setStyle("-fx-bar-fill: #4cffa4;");
        }
        Font labelFont = Font.font("Arial", 14); // Adjust the desired font size
        String labelStyle = "-fx-text-fill: #ffce08;"; // Adjust the desired label color
        xAxis.setTickLabelFont(labelFont);
        xAxis.setStyle(labelStyle);
        String css = "-fx-stroke: transparent; -fx-text-fill: #ffffff;"; // Adjust the desired label color
        //barChart.getStylesheets().add(getClass().getResource("charts.css").toExternalForm());
        xAxis.setStyle(css);
        // Add series to the chart
        barChart.getData().add(series);
        return barChart;
    }


    private static PieChart getPieChart(FlowDefinitionDTO selectedFlow) {
        PieChart pieChart = new PieChart();
        pieChart.getStyleClass().add("pie-chart");
        pieChart.setTitle("Flow Time Division");
        pieChart.setMaxHeight(270);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        double totalFlowTime = 0.0;
        for (StepUsageDeclarationDTO step : selectedFlow.getSteps()) {
            totalFlowTime += step.getAvgTime();
        }
        for (StepUsageDeclarationDTO step : selectedFlow.getSteps()) {
            double percentage = (step.getAvgTime() / totalFlowTime) * 100;
            PieChart.Data data = new PieChart.Data(step.getFinalStepName(), percentage);

            pieChartData.add(data);
        }
        pieChart.setData(pieChartData);
        pieChart.lookup(".chart-title").setStyle("-fx-text-fill: #ba00ff;");
        pieChart.lookup(".chart-pie-label-line").setStyle("-fx-stroke: #33a1ff; -fx-text-fill: #ffef00;");
        // pieChart.getStylesheets().add("charts.css");
        pieChart.lookup(".chart-legend").setStyle("-fx-text-fill: #4cffa4;");
        pieChart.lookup(".chart-pie-label").setStyle("-fx-fill: #fff90c;");
        return pieChart;
    }

    private void setListsView() {

        flowsList.setStyle(listStyle+";-fx-font-size: 14;-fx-alignment: center;-fx-font-weight: bold;-fx-control-inner-background: #24292e;");
        flowStatsList.setStyle(listStyle+";-fx-font-size: 12;-fx-alignment: center;-fx-control-inner-background: #24292e;");
        stepsList.setStyle(listStyle+";-fx-font-size: 14;-fx-alignment: center;-fx-font-weight: bold;-fx-control-inner-background: #24292e;");
        stepStatsList.setStyle(listStyle+";-fx-font-size: 12;-fx-alignment: center;-fx-control-inner-background: #24292e;");

    }

    private void Binds() {
        ListChangeListener<RadioButton> itemsChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    flowsList.setPrefHeight((flowsList.getItems().size() * flowsList.getFixedCellSize())+24);
                }
            }
        };
        flowsList.setFixedCellSize(24);
        flowsList.getItems().addListener((ListChangeListener<RadioButton>) itemsChangeListener);
        flowsList.setPrefHeight(flowsList.getItems().size() * flowsList.getFixedCellSize());

        ListChangeListener<String> itemsChangeListener2 = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    stepsList.setPrefHeight((stepsList.getItems().size() * stepsList.getFixedCellSize())+24);
                }
            }
        };
        stepsList.setFixedCellSize(24);
        stepsList.getItems().addListener((ListChangeListener<String>) itemsChangeListener2);
        stepsList.setPrefHeight(stepsList.getItems().size() * stepsList.getFixedCellSize());

        ListChangeListener<String> itemsChangeListener3 = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    stepStatsList.setPrefHeight((stepStatsList.getItems().size() * stepStatsList.getFixedCellSize())+24);
                }
            }
        };
        stepStatsList.setFixedCellSize(24);
        stepStatsList.getItems().addListener((ListChangeListener<String>) itemsChangeListener3);
        stepStatsList.setPrefHeight(stepStatsList.getItems().size() * stepStatsList.getFixedCellSize());

        ListChangeListener<String> itemsChangeListener4 = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    flowStatsList.setPrefHeight((flowStatsList.getItems().size() * flowStatsList.getFixedCellSize())+24);
                }
            }
        };
        flowStatsList.setFixedCellSize(24);
        flowStatsList.getItems().addListener((ListChangeListener<String>) itemsChangeListener4);
        flowStatsList.setPrefHeight(flowStatsList.getItems().size() * flowStatsList.getFixedCellSize());

    }

    private void setListeners() {
        //add listener to the flow list
//        flowsList.getItems().get(0).getToggleGroup().getProperties().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                // Get the selected flow from the newValue (RadioButton)
//                FlowDefinitionImpl selectedFlow = getFlowFromRadioButton(newValue);
//                // Update the stepsList based on the selected flow
//                updateStepsList(selectedFlow);
//            }
//        });



    }

    private void updateStepsList(FlowDefinitionDTO selectedFlow) {
        stepsList.getItems().clear();
        stepStatsList.getItems().clear();
        if (selectedFlow != null) {
            for (StepUsageDeclarationDTO step : selectedFlow.getSteps()) {
                stepsList.getItems().add(step.getFinalStepName());
                stepStatsList.getItems().add("Used "+step.getTimeUsed()+" times ||" + " Average time: "+GoodLookingDouble(step.getAvgTime())+" ms");
            }
            flowExecutionsSize.setText("There are "+selectedFlow.getSteps().size()+" Steps in this flow");
        } else {
            flowExecutionsSize.setText("Nothing Selected yet");
        }
    }

    private String GoodLookingDouble(double avgTime) {
        DecimalFormat df = new DecimalFormat("#######.##");
        return df.format(avgTime);
    }

    private FlowDefinitionDTO getFlowFromRadioButton(RadioButton pick) {
        String flowName = pick.getText();
        for (FlowDefinitionDTO flow : updatedFlows) {
            if (flow.getName().equals(flowName)) {
                return flow;
            }

        }
        return null;
    }
    private void updateLists() {
        if (updatedFlows.size()>0){
            ToggleGroup flowToggle = new ToggleGroup();
            for (FlowDefinitionDTO flow:updatedFlows){
                RadioButton flowSelection = new RadioButton(flow.getName());
                flowSelection.setToggleGroup(flowToggle);
                flowSelection.setOnAction(event -> {
                    RadioButton selectedFlowRadioButton = (RadioButton) flowToggle.getSelectedToggle();
                    if (selectedFlowRadioButton != null) {
                        FlowDefinitionDTO selectedFlow = getFlowFromRadioButton(selectedFlowRadioButton);
                        updateStepsList(selectedFlow);
                        currSelectedFlow = selectedFlow;
                        setCharts(selectedFlow);
                    }
                });

                flowsList.getItems().add(flowSelection);//check what happened her
            }
            flowDefinitionsSize.setText("There are "+updatedFlows.size()+" Flow Definitions");
            //set stats table here
            //maybe add listener to the second list
            for (FlowDefinitionDTO flow:updatedFlows){
                String singleFlowStats= "Used "+flow.getTimesUsed()+" times ||" + " Average time: "+GoodLookingDouble(flow.getAvgTime())+" ms";
                flowStatsList.getItems().add(singleFlowStats);
            }
            flowExecutionsSize.setText("");

        }else {
            flowDefinitionsSize.setText("There are no Flow Definitions");
        }
        //set stats table here

        //sync all menu buttons
        if (updatedFlows.size() == 0) {//both are empty
            popAlert();//show appropriate message
        }
    }
    private void setListsToVisible() {
        flowsList.setVisible(true);
        flowsList.setDisable(false);
        flowStatsList.setVisible(true);
        flowStatsList.setDisable(false);
        stepsList.setVisible(true);
        stepsList.setDisable(false);
        stepStatsList.setVisible(true);
        stepStatsList.setDisable(false);
    }

    private static void popAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Content");
        alert.setHeaderText(null);
        alert.setContentText("Nothing to show, consider adding a Data");
        alert.showAndWait();
    }

    @Override
    public void onLeave() {

    }

    @Override
    public void show() {

    }

    @Override
    public void setBodyController(bodyController body) {

    }

    @Override
    public void setFlowsDetails(List<FlowDefinitionDTO> list) {

    }

    @Override
    public void SetCurrentFlow(FlowDefinitionDTO flow) {

    }
}
