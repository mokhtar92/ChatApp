package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLFirstScreenController implements Initializable {

    private Stage myStage;
    private double xOffset = 0;
    private double yOffset = 0;
    

    @FXML
    private Label invalidIpMessage;

    @FXML
    private JFXTextField serverIpTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) invalidIpMessage.getScene().getWindow();
            }
        });
    }

    public void connectToServer() throws IOException {
        connect();
    }

    public void closeConnectToServerWindow() {
        myStage.close();
    }

    public void minimizeConnectToServerWindow() {
        myStage.setIconified(true);
    }

    public void moveServerWindow(MouseEvent event) {
        move(event);
    }
    
    public void setMousePosition(MouseEvent event){
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    
    //private implementation of methods
    private void connect() throws IOException {
        String receviedIP = serverIpTextField.getText().trim();
        final String regexIP = "^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$";

        if (receviedIP.matches(regexIP)) {
            invalidIpMessage.setVisible(false);
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLLoginScreen.fxml"));
                        Scene scene = new Scene(root);
                        myStage.setScene(scene);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLFirstScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
        } else {
            serverIpTextField.clear();
            invalidIpMessage.setVisible(true);
        }
    }

    private void move(MouseEvent event) {
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }

}
