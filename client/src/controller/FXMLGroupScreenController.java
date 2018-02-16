/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import factory.FriendCallBack2;
import factory.FriendCallback;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Service;
import model.UserGroups;
import model.UserSession;

/**
 * FXML Controller class
 *
 * @author Hanaa
 */
public class FXMLGroupScreenController implements Initializable {

    public ObservableList<User> data;
    private Service service = new Service();
    @FXML
    private ListView friendsListView;
    @FXML
    private ListView selectedMembers;
    @FXML
    private Label errorField;
    @FXML
    private TextField groupName;
    ArrayList<User> groupMembers = null;
    private Stage myStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) friendsListView.getScene().getWindow();
            }
        });
        groupMembers = new ArrayList<>();
        displayFriendList();
        friendsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                chooseMemebers();

            }
        });
    }

    private void displayFriendList() {
        ObservableList<User> friendsList = FXCollections.observableArrayList();
        friendsList.addAll(service.getFriendList());
        friendsListView.setItems(friendsList);
        friendsListView.setCellFactory(new FriendCallBack2());
    }

    private void displaySelectedMemebers() {
        ObservableList<User> friendsList = FXCollections.observableArrayList();
        friendsList.addAll(groupMembers);
        selectedMembers.setItems(friendsList);
        selectedMembers.setCellFactory(new FriendCallback());
    }

    private void chooseMemebers() {
        User user = new User();
        user = (User) friendsListView.getSelectionModel().getSelectedItem();
        if (!groupMembers.contains(user)) {
            groupMembers.add(user);
        } else {
            groupMembers.remove(user);
        }
        displaySelectedMemebers();
    }

    @FXML
    public void createGroup() throws RemoteException {
        String name = groupName.getText().trim();
        if (name != null && !name.equals("")) {
            if (groupMembers.size() > 1) {
                groupMembers.add(UserSession.getUser());
                Service.createGroup(groupMembers);
                int groupId = Service.getGroupId(groupMembers);
                UserGroups.addGroup(groupId + "", groupMembers);
                UserGroups.addGroupName(name);
                myStage.close();
            } else {
                errorField.setText("select more than one User");
            }
        } else {
            errorField.setText("Enter group Name");
        }
    }
}
