package app.MVC_controller;

import javafx.application.Platform;

import java.util.function.Consumer;

public class GuiAdapter {//binds the gui to the engine according to the MVC pattern

    private Consumer<Double> progressUpdater;
    private Consumer<String> statusUpdater;

    public GuiAdapter(Consumer<Double> progressUpdater, Consumer<String> statusUpdater) {
        this.progressUpdater = progressUpdater;
        this.statusUpdater = statusUpdater;
    }

    public void updateProgress(double progress) {
        Platform.runLater(() -> progressUpdater.accept(progress));
        //todo change to actual progress
    }

    public void updateStatus(String status) {
        Platform.runLater(() -> statusUpdater.accept(status));
    }




}
