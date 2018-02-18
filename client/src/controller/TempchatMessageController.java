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
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        /*Label label=new Label(message.getBody());
        vBox.getChildren().addAll(label);*/
    }

    public void createMessageStyle(Message styledMessage, User sender, String messageCSS) throws RemoteException, SQLException {
        Label label = new Label(styledMessage.getBody());

        HBox hBox;
        ImageView img;

        if (styledMessage.getFrom().equals(sender.getRecId().toString())) {
            label.getStyleClass().add("sender");
            label.setStyle(messageCSS);

            img = new ImageView(sender.getImgURL());
            img.setFitWidth(45);
            img.setFitHeight(45);

            hBox = new HBox(img, label);
            hBox.setAlignment(Pos.CENTER_LEFT);
            vBox.getChildren().addAll(hBox);

        } else {
            label.getStyleClass().add("receiver");
            label.setStyle(messageCSS);
            User receiver=Service.getUserById(Long.parseLong(styledMessage.getFrom()));
            
            img = new ImageView(receiver.getImgURL());
            img.setFitWidth(45);
            img.setFitHeight(45);

            hBox = new HBox(label, img);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            vBox.getChildren().addAll(hBox);
        }
    }

}
