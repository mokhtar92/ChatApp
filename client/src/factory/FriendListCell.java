/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import controller.FXMLChatScreenController;
import entity.User;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.Service;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FriendListCell extends ListCell<User> {

    FXMLChatScreenController controller;
    boolean status;

    FriendListCell(FXMLChatScreenController controller, boolean status) {
        this.controller = controller;
        this.status = status;
    }

    @Override
    protected void updateItem(User friend, boolean empty) {
        super.updateItem(friend, empty);

        if (friend != null && !empty) {
            Label username = new Label(friend.getFirstName());
            username.getStyleClass().add("listText");

            ImageView userImg = new ImageView(friend.getImgURL());
            userImg.setFitWidth(80);
            userImg.setFitHeight(80);

            ImageView statusImg = null;
            try {
                if (Service.isOnline(friend.getRecId() + "")) {
                    statusImg = new ImageView("/resources/available.png");
                } else {
                    statusImg = new ImageView("/resources/offline.png");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(FriendListCell.class.getName()).log(Level.SEVERE, null, ex);
            }
            statusImg.setFitWidth(25);
            statusImg.setFitHeight(25);

            HBox hBox = new HBox(userImg, username, statusImg);
            hBox.setPrefWidth(180);
            hBox.getStyleClass().add("friendList");
            if (status) {
                hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (Service.isOnline(friend.getRecId() + "")) {
                                        TabPane tabPane = controller.getChatTabPane();
                                        ObservableList<Tab> tabs = tabPane.getTabs();
                                        boolean flag = false;
                                        for (Tab tab : tabs) {
                                            if (tab.getId() != null) {
                                                if (tab.getId().equals(friend.getRecId() + "")) {
                                                    tabPane.getSelectionModel().select(tab);
                                                    flag = true;
                                                }
                                            }
                                        }
                                        if (!flag) {
                                            try {
                                                controller.insertNewChatTab(friend.getFirstName(), friend, false, null);
                                            } catch (RemoteException ex) {
                                                Logger.getLogger(FriendListCell.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                } catch (RemoteException ex) {
                                    Logger.getLogger(FriendListCell.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }
                });
            }
            setGraphic(hBox);

        } else {
            setGraphic(null);
        }
    }
}
