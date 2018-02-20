package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import entity.User;
import interfaces.ServerInt;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.ClientImpl;
import model.Service;
import model.UserSession;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLLoginController implements Initializable {

    private Stage myStage;
    private double xOffset = 0;
    private double yOffset = 0;
    private ServerInt server = null;
    Service service = new Service();

    @FXML
    private JFXPasswordField loginPasswordTextField;

    @FXML
    private JFXTextField loginEmailTextField;

    @FXML
    private Label invalidEmailPasswordMessage;

    @FXML
    private Label invalidOnlineUser;

    public FXMLLoginController() {
    }

    @FXML
    private void checkLogin(ActionEvent ev) {
        User user = new User();
        if(Service.getServer()!=null){
        String email = loginEmailTextField.getText().toLowerCase();
        String password = loginPasswordTextField.getText();
        user.setEmail(email);
        user.setPassword(password);
        Long userId = service.checkLogin(user);
        if (userId != null && userId != 0L) {
            invalidEmailPasswordMessage.setVisible(false);
            try {
                user = Service.getUser(email, password);
                UserSession.setUser(user);
                if (!Service.isOnline(user.getRecId() + "")) {
                    Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLChatScreen.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    invalidOnlineUser.setVisible(true);
                    invalidEmailPasswordMessage.setVisible(false);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        } else {
            invalidEmailPasswordMessage.setVisible(true);
            invalidOnlineUser.setVisible(false);
        }

    }else{
             Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLServerOfflineScreen.fxml"));
                        Scene scene = new Scene(root);
                        myStage.setScene(scene);

                    } catch (IOException ex) {
                        Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) invalidEmailPasswordMessage.getScene().getWindow();
            }
        });
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

    public void redirectToSignUp() {
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
    private void move(MouseEvent event) {
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }

}
