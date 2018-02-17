/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Chat;
import entity.Message;
import entity.NotificationStatus;
import entity.User;
import entity.XmlHandler;
import factory.FriendCallback;
import factory.StatusCallback;
import factory.StatusListCell;
import interfaces.ClientInt;
import interfaces.NotificationInt;
import interfaces.ServerInt;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXBException;
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
    private Service service = new Service();
    Message message;
    private ServerInt server = null;
    private ClientInt client = null;

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
    private ListView<User> friendsListView;

    @FXML
    private ColorPicker messageColorPicker;

    @FXML
    private ComboBox<String> fontSizeComboBox;

    @FXML
    private ComboBox<String> fontStyleComboBox;

    @FXML
    private ComboBox<String> fontFamilyComboBox;

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
        setUserProfile();
        setMessageFormatter();

        server = Service.getServer();

        user = UserSession.getUser();

        try {
            client = new ClientImpl(this);
            Service.register(client, user);

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
      

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

    public void getAnnoncement(String message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NotificationInt impl = new NotificationImpl();
                impl.createNotification("Acconcement", message, "resources/chat_logo.png");
                adsArea.setText(adsArea.getText() + "\n" + message);
            }
        });

    }

    public void getNotification(int Status, User user) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NotificationInt impl = new NotificationImpl();
                if (Status == NotificationStatus.onlineStatus) {
                    impl.createNotification("Acconcement", user.getFirstName() + " become online", "resources/chat_logo.png");
                }
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

    public void addFriend() {

    }
    @FXML
    public void addGroupChat(MouseEvent event) {
      /*  Platform.runLater(new Runnable() {
            @Override
            public void run() { */
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLGroupScreen.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Create New Group");
            stage.show();
               
       
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//}});
    }

    public void viewFriendRequests() {

    }

    public void saveChatHistory() throws JAXBException, IOException {
   
     // for test list    
    List <String> ToList = new ArrayList<>();
    ToList.add("mohamed");
    
   // messages for test
    Message m = new Message("Ahmed", ToList, "hi mohamed ..", "12", "red","20-2-2012","20:12", "ARIAL", "bold");
    Message m2 = new Message("Ahmed", ToList, "hi khaled ..", "12", "red","20-2-2012","20:12", "ARIAL", "bold");
    Message m3 = new Message("Ahmed", ToList, "hi sayed ..", "12", "red","20-2-2012","20:12", "ARIAL", "bold");
    
    //chat object to contain messages
    Chat myChat = new Chat(); 
    myChat.getMessage().add(m);
    myChat.getMessage().add(m2);
    myChat.getMessage().add(m3);
    
        // create fileChooser save Dialoge
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.getExtensionFilters().add(extFilter);
       
        File file = fileChooser.showSaveDialog(myStage);
        
      // calling saving method
       XmlHandler xmlHandler = new XmlHandler();
      xmlHandler.SaveXml(file, myChat.getMessage());
        
    
          
    }

    public void sendFile() {

    }

    public void closeChatWindow() throws RemoteException {
        Service.Unregister(client, UserSession.getUser());
        myStage.close();
        System.exit(0);
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
        options.addAll("available", "away", "busy");
        userAvailabilityComboBox.setItems(options);
        userAvailabilityComboBox.setButtonCell(new StatusListCell());
        userAvailabilityComboBox.setCellFactory(new StatusCallback());
    }

    private void displayFriendList() {
        ObservableList<User> friendsList = FXCollections.observableArrayList();
        friendsList.addAll(service.getFriendList());
        friendsListView.setItems(friendsList);
        friendsListView.setCellFactory(new FriendCallback());
    }

    private void move(MouseEvent event) {
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }

    private void setUserProfile() {
        User currentUser = UserSession.getUser();
        if (currentUser != null) {
            userImage.setImage(new Image(currentUser.getImgURL()));
            userName.setText(currentUser.getFirstName());
            userName.setStyle("-fx-text-alignment: center;");
            userAvailabilityComboBox.setValue(currentUser.getMyStatus());
        }

    }

     private void setMessageFormatter(){
        fontFamilyComboBox.getItems().addAll("Arial", "Segoe UI Semibold", "Bell MT", "Brush Script MT");
        fontFamilyComboBox.setValue("Arial");
        fontFamilyComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-font-family: \""+ newValue + "\";");
            }
        });
        
        fontSizeComboBox.getItems().addAll("10", "12", "14", "16");
        fontSizeComboBox.setValue("10");
        fontSizeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-font-size: "+ newValue + "pt;");
            }
        });
       
        fontStyleComboBox.getItems().addAll("normal", "bold","italic");
        fontStyleComboBox.setValue("regular");
        fontStyleComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                if (newValue.equals("bold")) {
                    sendTextField.setStyle(previousStyle + "-fx-font-weight: " + newValue +";");
                } else if (newValue.equals("italic")) {
                    sendTextField.setStyle(previousStyle + "-fx-font-style: " + newValue +";");
                } else {
                    sendTextField.setStyle("-fx-font-style: " + newValue +";");
                }
            }
        });
        
        messageColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String hexColor = Integer.toHexString(messageColorPicker.getValue().hashCode()); 
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-text-inner-color: #" + hexColor +";");
            }
        });
    }

}
