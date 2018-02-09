package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
public class FXMLLoginController implements Initializable{

    private Stage myStage;
    private double xOffset = 0;
    private double yOffset = 0;
    
    
    @FXML
    private JFXPasswordField loginPasswordTextField;

    @FXML
    private JFXTextField loginEmailTextField;

    @FXML
    private Label invalidEmailPasswordMessage;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) invalidEmailPasswordMessage.getScene().getWindow();
            }
        });
    }
    
    public void loginToServer() {
        login();
    }
    
    public void moveLoginWindow(MouseEvent event) {
        move(event);
    }

    public void closeLoginWindow(MouseEvent event) {
        myStage.close();
    }

    public void minimizeLoginWindow(MouseEvent event) {
        myStage.setIconified(true);
    }
    
    public void setMousePosition(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    
    
    //private implementation of methods
    private void login() {
        String userInputEmail = loginEmailTextField.getText().trim();
        final String regexEmail = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        if (userInputEmail.matches(regexEmail)) {
            invalidEmailPasswordMessage.setVisible(false);

        } else {
            loginEmailTextField.clear();
            loginPasswordTextField.clear();
            invalidEmailPasswordMessage.setVisible(true);
        }
    }

    private void move(MouseEvent event) {
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }

    
    
}
