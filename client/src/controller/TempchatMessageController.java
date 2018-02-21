/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Message;
import entity.User;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Service;

/**
 * FXML Controller class
 *
 * @author Hanaa
 */
public class TempchatMessageController implements Initializable {

    @FXML
    private VBox vBox;
    ArrayList<Message> messages = new ArrayList<>();

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void getMessages(Message message) {
        messages.add(message);
    }

    TempchatMessageController() {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    public void createMessageStyle(Message styledMessage, User sender) throws RemoteException, SQLException {
        
        Label label = new Label(styledMessage.getBody());

        HBox hBox;
        ImageView img;
        String fontColor = "-fx-text-fill: #" + styledMessage.getFontColor() + ";";
        String fontSize = "-fx-font-size: " + styledMessage.getFontSize() + "pt;";
        String fontFamily = "-fx-font-family: \"" + styledMessage.getFontFamily() + "\";";
        String fontStyle = "";

        switch (styledMessage.getFontStyle()) {
            case "bold":
                fontStyle = "-fx-font-weight: " + styledMessage.getFontStyle() + ";";
                break;

            default:
                fontStyle = "-fx-font-style: " + styledMessage.getFontStyle() + ";";
        }
        
        String cssStyle = fontColor + fontSize + fontFamily + fontStyle;

        if (styledMessage.getFrom().equals(sender.getRecId().toString())) {
            label.setStyle(cssStyle);
            label.getStyleClass().add("sender");
            img = new ImageView(sender.getImgURL());
            img.setFitWidth(45);
            img.setFitHeight(45);

            hBox = new HBox(img, label);
            hBox.setAlignment(Pos.CENTER_LEFT);
            vBox.getChildren().addAll(hBox);

        } else {
            label.setStyle(cssStyle);
            label.getStyleClass().add("receiver");
           
            User receiver = Service.getUserById(Long.parseLong(styledMessage.getFrom()));
            img = new ImageView(receiver.getImgURL());
            img.setFitWidth(45);
            img.setFitHeight(45);

            hBox = new HBox(label, img);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            vBox.getChildren().addAll(hBox);
        }
    }
}
