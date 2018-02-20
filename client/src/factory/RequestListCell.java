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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
            Label username = new Label(request.getFirstName());
            username.getStyleClass().add("listText");

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
            hBox.setPrefWidth(180);
            hBox.getStyleClass().add("requestList");
            setGraphic(hBox);

        } else {
            setGraphic(null);
        }

    }

}
