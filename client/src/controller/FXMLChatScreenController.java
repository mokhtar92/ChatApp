/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Chat;
import entity.FileSender;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.Node;
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
                message.setFrom(user.getRecId() + "");
                try {
                    Tab tab = chatTabPane.getSelectionModel().getSelectedItem();

                    message.setBody(msg);

                    if (tab.getId().contains("group")) {
                        sendTextField.setText("");
                        Service.tellGroup(message, tab.getId());
                        //message.getTo().add(user.getRecId()+"");
                    } else {
                        message.getTo().add(tab.getId());
                        sendTextField.setText("");
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
                        } else {
                            if (message.getFrom().equals(user.getRecId() + "")) {

                                if (tab.getId().equals(message.getTo().get(0))) {
                                    Oneflag = true;

                                }
                            } else if (message.getTo().get(0).equals(user.getRecId() + "")) {

                                if (tab.getId().equals(message.getFrom())) {
                                    Oneflag = true;

                                }
                            }
                        }
                    }
                }
                if (group != null) {
                    if (!flag) {
                        try {

                            insertNewChatTab(Service.getGroupName(group), user, true, group);
                        } catch (RemoteException ex) {
                            Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    if (!Oneflag) {
                        if (!Oneflag && message.getTo().get(0) != null && message.getFrom() != null) {
                            if (!message.getFrom().equals(user.getRecId())) {
                                try {

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
                    }
                }
                for (Tab tab : tabs) {

                    if (tab.getId() != null) {
                        if (group == null) {
                            if ((tab.getId().equals(message.getFrom()) || tab.getId().equals(message.getTo().get(0))) && group == null) {
                                try {
                                    controllers.get(i).createMessageStyle(message, user, sendTextField.getStyle());
                                } catch (RemoteException ex) {
                                    Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (SQLException ex) {
                                    Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                controllers.get(i).getMessages(message);
                            }
                        } else {

                            if (tab.getId().equals(group)) {
                                try {
                                    controllers.get(i).createMessageStyle(message, user, sendTextField.getStyle());
                                    //controllers.get(i).getMessages(message);
                                } catch (RemoteException ex) {
                                    Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (SQLException ex) {
                                    Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        }

                        i++;
                    }

                }
                //chatVBox.getChildren().add(label);
            }
        });

    }

    public void getAnnouncement(String message) {
        if (message.equals("###!!!")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    myStage.close();
                }
            });
            
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    NotificationInt impl = new NotificationImpl();
                    impl.createNotification("Announcement", message, "resources/announce.png");
                    adsArea.setText(message);
                }
            });
        }
    }

    public TabPane getChatTabPane() {
        return chatTabPane;
    }

    public void getNotification(int status, User user) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NotificationInt impl = new NotificationImpl();
                if (status == NotificationStatus.onlineStatus) {
                    impl.createNotification("Acconcement", user.getFirstName() + " become online", user.getImgURL());
                }
            }
        });

    }

    public void getFileNotification(int status, User user) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NotificationInt impl = new NotificationImpl();
                if (status == NotificationStatus.fileSendStatus) {
                    impl.createNotification("File Sharing", user.getFirstName() + " send file to you", user.getImgURL());
                }
            }
        });

    }

    public void requestNotification(int status, User user) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NotificationInt impl = new NotificationImpl();
                if (status == NotificationStatus.friendRequest) {
                    System.out.println(user);
                    impl.createNotification("Acconcement", user.getFirstName() + " you have new friend request", user.getImgURL());
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
        if (result.isPresent() && service.isEmailExist(result.get().toLowerCase())) {
            if (!isValidRequest(result.get().toLowerCase(), getRequestedList)) {
                notification.createNotification("Alert", "You have already sent friend request to this person", "/resources/decline.png");
            } else if (!isValidRequest(result.get().toLowerCase(), allRequest)) {
                notification.createNotification("Alert", "You have invitation from this person please check your requests", "/resources/decline.png");
            } else if (!isValidRequest(result.get().toLowerCase(), service.getFriendList())) {
                notification.createNotification("Alert", "You have this friend in your contacts", "/resources/decline.png");
            } else if (UserSession.getUser().getEmail().toLowerCase().equalsIgnoreCase(result.get().toLowerCase())) {
                notification.createNotification("Alert", "Can't send friend request to your account", "/resources/decline.png");
            } else {
                service.sendFriendRequest(result.get().toLowerCase());
                notification.createNotification("Alert", "Your friend request sent", "/resources/accept.png");
            }
        } else if (result.get().equals("")) {
            notification.createNotification("Alert", "Closed", "/resources/decline.png");
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
            stage.setResizable(false);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void viewFriendRequests() {

    }

    public void saveChatHistory() throws JAXBException, IOException {
        Tab currentTab = chatTabPane.getSelectionModel().getSelectedItem();
        if (!currentTab.getId().contains("group")) {
            int i = 0;
            ArrayList<Message> messages = null;
            ObservableList<Tab> tabs = chatTabPane.getTabs();
            Tab selectedTab = chatTabPane.getSelectionModel().getSelectedItem();
            for (Tab tab : tabs) {

                if (tab.getId() != null) {
                    if (tab.getId().equals(selectedTab.getId())) {
                        messages = controllers.get(i).getMessages();
                    }
                    i++;
                }

            }

            //chat object to contain messages
            Chat myChat = new Chat();
            myChat.getMessage().addAll(messages);

            // create fileChooser save Dialoge
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("xml files (*.xml)", "xml");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(myStage);

            if (file != null) {
                // calling saving method
                XmlHandler xmlHandler = new XmlHandler();
                xmlHandler.SaveXml(file, myChat.getMessage());
            }
        }
    }

    public void sendFile() throws RemoteException {
        Tab selectedTab = chatTabPane.getSelectionModel().getSelectedItem();
        if (!selectedTab.getId().contains("group")) {
            message.setFrom(UserSession.getUser().getRecId() + "");
            message.getTo().add(chatTabPane.getSelectionModel().getSelectedItem().getId());
            if (Service.isOnline(message.getTo().get(0))) {
                chooseFile(message);
            }

        }
    }

    @FXML
    void chooseFile(Message message) throws RemoteException {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            FileSender fileSender = new FileSender();
            fileSender.setFile(file);
            fileSender.setMessage(message);
            Service.sendFile(fileSender);
        }

    }

    public void downloadFile(int file_id) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FileSender fileSender = ClientImpl.files.get(file_id + "");
                File file = fileSender.getFile();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName(file.getName());
                File pfile = fileChooser.showSaveDialog(null);
                String path = pfile.getAbsolutePath();
                Thread tr = new Thread(() -> {
                    try {
                        FileInputStream in = null;
                        if (path == null) {
                            return;
                        }
                        in = new FileInputStream(file);
                        byte[] data = new byte[1024 * 1024];
                        int dataLength = in.read(data);
                        boolean append = false;
                        while (dataLength > 0) {
                            client.reciveFile(path, file.getName(), append, data, dataLength);
                            dataLength = in.read(data);
                            append = true;
                        }

                    } catch (RemoteException ex) {
                        Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                tr.start();
            }
        });

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

    public void updateFriendList() {
        displayFriendList();
    }

    public void updateFriendRequest() {
        displayRequestList();
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
        friendsListView.setCellFactory(new FriendCallback(this, true));
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
