package controller;

import com.jfoenix.controls.JFXPasswordField;
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
    
    public void redirectToSignUp(){
        Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLSignUp.fxml"));
                        Scene scene = new Scene(root);
                        myStage.setScene(scene);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLFirstScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
    }
    
    
    //private implementation of methods
    private void login() {
        String userInputEmail = loginEmailTextField.getText().trim();
        final String REGEX_EMAIL = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        if (userInputEmail.matches(REGEX_EMAIL)) {
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
