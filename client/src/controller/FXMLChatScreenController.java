/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Message;
import entity.User;
import factory.FriendCallback;
import factory.StatusCallback;
import factory.StatusListCell;
import interfaces.ClientInt;
import interfaces.NotificationInt;
import interfaces.ServerInt;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ClientImpl;
import model.NotificationImpl;
import model.Service;
import model.UserSession;

/**
 * FXML Controller class
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLChatScreenController implements Initializable {

    private Stage myStage;
    private double xOffset = 0;
    private double yOffset = 0;
    private User user = null;
    Message message;

    @FXML
    private VBox chatVBox;

    @FXML
    private ImageView userImage;

    @FXML
    private TextArea userStatusTextArea;

    @FXML
    private Label adsArea;

    @FXML
    private TabPane chatTabPane;

    @FXML
    private Label userName;
    @FXML
    private TextField sendTextField;

    @FXML
    private ComboBox<String> userAvailabilityComboBox;

    @FXML
    private ListView<String> friendsListView;
    private ServerInt server = null;
    private ClientInt client = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) userImage.getScene().getWindow();
            }
        });
        displayComboBox();
        displayFriendList();
        server = Service.getServer();
    
        user = UserSession.getUser();

        try {
            client = new ClientImpl(this);
            Service.register(client, user);

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        message = new Message();
        //Just for Testing :))
        message.setFrom("1");
        message.setTo("21");

    }

    @FXML
    public void sendMessage(KeyEvent event) {
        String msg = sendTextField.getText().trim();
        if (msg != null) {
            if (event.getCode().equals(KeyCode.ENTER)) {

                try {
                    message.setBody(msg);

                    Service.tellOne(message);

                } catch (RemoteException ex) {
                    Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);

                }

            }

        }
    }

    public void getMessage(Message message) {
        Label label = new Label(message.getBody());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatVBox.getChildren().add(label);
            }
        });

    }
    public void getAnnoncement(String message){
        System.out.println(message);
         Platform.runLater(new Runnable() {
            @Override
            public void run() {
              NotificationInt impl = new NotificationImpl() ;
                impl.createNotification("Acconcement", message, "resources/chat_logo.png");
                adsArea.setText(adsArea.getText()+"\n"+message);
            }
        });
        
    }
    @FXML
    public void onStatusChanged(ActionEvent event) {
        try {
            String status = userAvailabilityComboBox.getValue();
            server.ChangeStatus(user, status);
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveChatHistory() {

    }

    public void sendFile() {

    }

    public void closeChatWindow() {
        myStage.close();
    }

    public void minimizeChatWindow() {
        myStage.setIconified(true);
    }

    public void moveChatWindow(MouseEvent event) {
        move(event);
    }

    public void setMousePosition(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    //private method implementation
    private void displayComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll("online", "away", "busy");
        userAvailabilityComboBox.setItems(options);
        userAvailabilityComboBox.setButtonCell(new StatusListCell());
        userAvailabilityComboBox.setCellFactory(new StatusCallback());
    }

    private void displayFriendList() {
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll("online", "away", "busy");
        friendsListView.setItems(options);
        friendsListView.setCellFactory(new FriendCallback());
    }

    private void move(MouseEvent event) {
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }
 
}
