/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import entity.User;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.Service;

/**
 *
 * @author Hanaa
 */
public class FriendListCell2 extends ListCell<User> {

    @Override
    protected void updateItem(User friend, boolean empty) {
        super.updateItem(friend, empty);

        if (friend != null && !empty) {

            Label username = new Label(friend.getFirstName() + " " + friend.getLastName());
            Label label = new Label("not");
            ImageView userImg = new ImageView(friend.getImgURL());
            userImg.setFitWidth(80);
            userImg.setFitHeight(80);
             ImageView statusImg = null;
            try {
                if(Service.isOnline(friend.getRecId()+"")){
                    statusImg=new ImageView("/resources/available.png");
                }else{
                statusImg=new ImageView("/resources/offline.png");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(FriendListCell.class.getName()).log(Level.SEVERE, null, ex);
            }
            statusImg.setFitWidth(25);
            statusImg.setFitHeight(25);
            HBox hBox = new HBox(userImg, username,statusImg);
            hBox.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    if (label.getText().equals("not")) {
                        setStyle("-fx-control-inner-background:#0096c9");
                        label.setText("yes");
                    
                    } else {
                        setStyle("-fx-control-inner-background: white");
                        label.setText("not");
                    }
                }
            });
            setGraphic(hBox);

        } else {
            setGraphic(null);
        }
    }
}
