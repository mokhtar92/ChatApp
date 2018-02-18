/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import controller.FXMLChatScreenController;
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
public class RequestListCell extends ListCell<User> {

    FXMLChatScreenController controller;
    Service service = new Service();

    RequestListCell(FXMLChatScreenController controller) {
        this.controller = controller;
    }

    @Override
    protected void updateItem(User request, boolean empty) {
        super.updateItem(request, empty);

        if (request != null) {
            Text username = new Text(request.getFirstName() + " " + request.getLastName());
            username.getStyleClass().add("listText");
            username.setTextAlignment(TextAlignment.CENTER);

            ImageView userImage = new ImageView(request.getImgURL());
            userImage.setFitWidth(50);
            userImage.setFitHeight(50);

            ImageView acceptButton = new ImageView("/resources/accept.png");
            acceptButton.setFitWidth(25);
            acceptButton.setFitHeight(25);
            acceptButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    service.acceptFriendRequest(request.getRecId());
                    controller.displayRequestList();
                }
            });

            ImageView declineButton = new ImageView("/resources/decline.png");
            declineButton.setFitWidth(25);
            declineButton.setFitHeight(25);
            declineButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    service.deleteFriendRequest(request.getRecId());
                    controller.displayRequestList();
                }
            });

            HBox hBox = new HBox(userImage, username, acceptButton, declineButton);
            setGraphic(hBox);

        } else {
            setGraphic(null);
        }

    }

}
