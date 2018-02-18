/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLServerOfflineController implements Initializable {

    private Stage myStage;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Label serverIsOfflineLable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) serverIsOfflineLable.getScene().getWindow();
            }
        });
    }

    @FXML
    void moveServerOfflineWindow(MouseEvent event) {
        move(event);
    }

    @FXML
    void setMousePosition(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void closeServerOfflineWindow() {
        myStage.close();
        System.exit(0);
    }

    @FXML
    void minimizeServerOfflineWindow() {
        myStage.setIconified(true);
    }

    //private methods implementation
    private void move(MouseEvent event) {
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }
}
