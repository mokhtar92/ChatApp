/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import factory.FriendCallback;
import factory.StatusCallback;
import factory.StatusListCell;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLChatScreenController implements Initializable {

    private Stage myStage;
    private double xOffset = 0;
    private double yOffset = 0;
    
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
    private ComboBox<String> userAvailabilityComboBox;
    
    @FXML
    private ListView<String> friendsListView;
    
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
    }    
    
    public void sendMessage() {

    }

    public void onStatusChanged() {

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
    private void displayComboBox(){
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll("online", "away", "busy");
        userAvailabilityComboBox.setItems(options);
        userAvailabilityComboBox.setButtonCell(new StatusListCell());
        userAvailabilityComboBox.setCellFactory(new StatusCallback());
    }
    
    private void displayFriendList(){
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll("online", "away", "busy");
        friendsListView.setItems(options);
        friendsListView.setCellFactory(new FriendCallback());
    }
    
    private void move(MouseEvent event){
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }
    
}
