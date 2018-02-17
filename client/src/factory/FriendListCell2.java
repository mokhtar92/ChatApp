/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import entity.User;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author Hanaa
 */
public class FriendListCell2 extends ListCell<User> {
    boolean flag=false;

    @Override
    protected void updateItem(User friend, boolean empty) {
        super.updateItem(friend, empty);

        if (friend != null && !empty) {
            Text username = new Text(friend.getFirstName() + " " + friend.getLastName());
            ImageView userImg = new ImageView(friend.getImgURL());
            userImg.setFitWidth(80);
            userImg.setFitHeight(80);
            
            ImageView statusImg = new ImageView("/resources/"+friend.getMyStatus()+".png");
            statusImg.setFitWidth(25);
            statusImg.setFitHeight(25);
            
            HBox hBox = new HBox(userImg, username);
            hBox.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    if(!flag){
                    setStyle("-fx-control-inner-background:#0096c9");
                        flag=true;
                    }else{
                    setStyle("-fx-control-inner-background: white");
                        flag=false;
                    }
                }
            });
            setGraphic(hBox);

        } else {
            setGraphic(null);
        }
    }

}
