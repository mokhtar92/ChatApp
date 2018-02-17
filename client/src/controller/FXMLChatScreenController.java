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
import factory.RequestCallback;
import factory.StatusCallback;
import factory.StatusListCell;
import interfaces.ClientInt;
import interfaces.NotificationInt;
import interfaces.ServerInt;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXBException;
import model.ClientImpl;
import model.NotificationImpl;
import model.Service;
import model.UserGroups;
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
    private NotificationImpl notification = new NotificationImpl();
    private ArrayList<TempchatMessageController> controllers = new ArrayList<>();

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
    private ListView<User> requestsListView;

    @FXML
    private ColorPicker messageColorPicker;

    @FXML
    private ComboBox<String> fontSizeComboBox;

    @FXML
    private ComboBox<String> fontStyleComboBox;

    @FXML
    private ComboBox<String> fontFamilyComboBox;
    
    @FXML
    private Tab friendListTab;

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
        displayRequestList();

        message = new Message();
        server = Service.getServer();
        user = UserSession.getUser();
        try {
            client = new ClientImpl(this);
            Service.register(client, user);

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Just for Testing :))
        message.setFrom(user.getRecId() + "");

    }

    @FXML
    public void sendMessage(KeyEvent event) {
        String msg = sendTextField.getText().trim();
        if (msg != null) {
            if (event.getCode().equals(KeyCode.ENTER)) {

                try {
                    Tab tab = chatTabPane.getSelectionModel().getSelectedItem();

                    message.setBody(msg);

                    if (tab.getId().contains("group")) {
                        Service.tellGroup(message, tab.getId());
                    } else {
                        message.getTo().add(tab.getId());
                        Service.tellOne(message);
                    }
                    System.out.println(tab.getId());

                } catch (RemoteException ex) {
                    Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);

                }

            }

        }
    }

    public void getMessage(Message message, String group) {
        Label label = new Label(message.getBody());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<Tab> tabs = chatTabPane.getTabs();
                int i = 0;
                boolean flag = false;
                boolean Oneflag = false;
                for (Tab tab : tabs) {
                    if (tab.getId() != null) {
                        if (group != null) {
                            if (tab.getId().equals(group)) {
                                flag = true;
                            }
                        }
                        if (message.getFrom().equals(user.getRecId() + "")) {
                            System.out.println("enter1");
                            if (tab.getId().equals(message.getTo().get(0))) {
                                Oneflag = true;
                                System.out.println("enter1");
                            }
                        } else if (message.getTo().get(0).equals(user.getRecId() + "")) {
                            System.out.println("enter2");
                            if (tab.getId().equals(message.getFrom())) {
                                Oneflag = true;
                                System.out.println("enter2");
                            }
                        }

                    }
                }
                if (!flag && group != null) {
                    try {
                        System.out.println("group name" + UserGroups.getGroupName(group));
                        System.out.println("group id " + group);
                        insertNewChatTab(Service.getGroupName(group), user, true, group);
                    } catch (RemoteException ex) {
                        Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (!Oneflag && message.getTo().get(0) != null && message.getFrom() != null) {
                    if (!message.getFrom().equals(user.getRecId())) {
                        try {
                            System.out.println("enter individual tab");
                            User reciver = Service.getUserById(Long.parseLong(message.getTo().get(0)));
                            User sender = Service.getUserById(Long.parseLong(message.getFrom()));
                            insertNewChatTab(sender.getFirstName(), sender, Oneflag, null);
                        } catch (RemoteException ex) {

                            Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                for (Tab tab : tabs) {

                    if (tab.getId() != null) {
                        if ((tab.getId().equals(message.getFrom()) || tab.getId().equals(message.getTo().get(0))) && group == null) {
                            controllers.get(i).showMessage(message);
                            System.out.println("tell one");
                        } else {

                            if (tab.getId().equals(group)) {
                                controllers.get(i).showMessage(message);
                                System.err.println("tell group");

                            }
                        }

                        i++;
                    }

                }
                chatVBox.getChildren().add(label);
            }
        });

    }

    public void getAnnoncement(String message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NotificationInt impl = new NotificationImpl();
                impl.createNotification("Acconcement", message, "resources/announce.png");
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

    /**
     * ************** Send Friend Request **************
     */
    private boolean isValidRequest(String email, ArrayList<User> searchList) {
        boolean isExist = true;
        for (User s : searchList) {
            if (s.getEmail().toLowerCase().equalsIgnoreCase(email.toLowerCase())) {
                isExist = false;
            }
        }
        return isExist;
    }

    @FXML
    public void addFriend() {
        ArrayList<User> getRequestedList = service.getRequestedFriend();
        ArrayList<User> allRequest = service.getAllRequest();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Friend");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Friend Email:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && service.isEmailExist(result.get())) {
            if (!isValidRequest(result.get(), getRequestedList)) {
                notification.createNotification("Alert", "You have already sent friend request to this person", "/resources/decline.png");
            } else if (!isValidRequest(result.get(), allRequest)) {
                notification.createNotification("Alert", "You have invitation from this person please check your requests", "/resources/decline.png");
            } else if (!isValidRequest(result.get(), service.getFriendList())) {
                notification.createNotification("Alert", "You have this friend in your contacts", "/resources/decline.png");
            } else if (UserSession.getUser().getEmail().toLowerCase().equalsIgnoreCase(result.get().toLowerCase())) {
                notification.createNotification("Alert", "Can't send friend request to your account", "/resources/decline.png");
            } else {
                service.sendFriendRequest(result.get());
                notification.createNotification("Alert", "Your friend request sent", "/resources/accept.png");
            }

        } else {
            notification.createNotification("Alert", "Enter valid user email", "/resources/decline.png");
        }
    }

    @FXML
    public void addGroupChat(MouseEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLGroupScreen.fxml"));
            FXMLGroupScreenController groupController = new FXMLGroupScreenController(this);
            loader.setController(groupController);
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Create New Group");
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void viewFriendRequests() {

    }

    public void saveChatHistory() throws JAXBException, IOException {

        // for test list    
        List<String> ToList = new ArrayList<>();
        ToList.add("mohamed");

        // messages for test
        Message m = new Message("Ahmed", ToList, "hi mohamed ..", "12", "red", "20-2-2012", "20:12", "ARIAL", "bold");
        Message m2 = new Message("Ahmed", ToList, "hi khaled ..", "12", "red", "20-2-2012", "20:12", "ARIAL", "bold");
        Message m3 = new Message("Ahmed", ToList, "hi sayed ..", "12", "red", "20-2-2012", "20:12", "ARIAL", "bold");

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
        Service.Unregister(client, user);
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

    public void insertNewChatTab(String name, User user, boolean isGroup, String groupId) throws RemoteException {
        Tab newChaTab = new Tab(name);
        if (!isGroup) {
            newChaTab.setId(user.getRecId() + "");
        } else {

            newChaTab.setId(groupId);

        }
        newChaTab.setClosable(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TempchatMessage.fxml"));
        TempchatMessageController tabController = new TempchatMessageController();
        loader.setController(tabController);
        try {
            newChaTab.setContent(loader.load());
        } catch (IOException ex) {
            Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controllers.add(tabController);
        chatTabPane.getTabs().add(newChaTab);
        chatTabPane.getSelectionModel().select(newChaTab);
    }
    public void updateFriendList(){
        displayFriendList();
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
        friendsListView.setCellFactory(new FriendCallback(this));
    }

    public void displayRequestList() {
        ObservableList<User> friendRequests = FXCollections.observableArrayList();
        friendRequests.addAll(service.getAllRequest());
        requestsListView.setItems(friendRequests);
        requestsListView.setCellFactory(new RequestCallback(this));
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

    private void setMessageFormatter() {
        fontFamilyComboBox.getItems().addAll("Arial", "Segoe UI Semibold", "Bell MT", "Brush Script MT");
        fontFamilyComboBox.setValue("Arial");
        fontFamilyComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-font-family: \"" + newValue + "\";");
            }
        });

        fontSizeComboBox.getItems().addAll("10", "12", "14", "16");
        fontSizeComboBox.setValue("10");
        fontSizeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-font-size: " + newValue + "pt;");
            }
        });

        fontStyleComboBox.getItems().addAll("normal", "bold", "italic");
        fontStyleComboBox.setValue("regular");
        fontStyleComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                if (newValue.equals("bold")) {
                    sendTextField.setStyle(previousStyle + "-fx-font-weight: " + newValue + ";");
                } else if (newValue.equals("italic")) {
                    sendTextField.setStyle(previousStyle + "-fx-font-style: " + newValue + ";");
                } else {
                    sendTextField.setStyle("-fx-font-style: " + newValue + ";");
                }
            }
        });

        messageColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String hexColor = Integer.toHexString(messageColorPicker.getValue().hashCode());
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-text-inner-color: #" + hexColor + ";");
            }
        });
    }

}
