/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import entity.User;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.Service;


/**
 *
 * @author Ahmed_Mokhtar
 */
public class RequestListCell extends ListCell<User>{

    Service service = new Service();
    @Override
    protected void updateItem(User request, boolean empty) {
        super.updateItem(request, empty);
        
        if (request != null) {
            Text username = new Text(request.getFirstName()+ " " + request.getLastName());
            username.setTextAlignment(TextAlignment.CENTER);
            
            ImageView userImage = new ImageView(request.getImgURL());
            userImage.setFitWidth(80);
            userImage.setFitHeight(80);
            
            ImageView acceptButton = new ImageView("/resources/accept.png");
            acceptButton.setFitWidth(25);
            acceptButton.setFitHeight(25);
            acceptButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    service.acceptFriendRequest(request.getRecId());
                }
            });
            
            ImageView declineButton = new ImageView("/resources/decline.png");
            declineButton.setFitWidth(25);
            declineButton.setFitHeight(25);
            declineButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    service.deleteFriendRequest(request.getRecId());
                }
            });
            
            
            HBox hBox = new HBox(acceptButton, declineButton);
            
            VBox vBox = new VBox(userImage, username, hBox);
            
            setGraphic(vBox);
            
        } else {
            setGraphic(null);
        }
       
    }
    
}
