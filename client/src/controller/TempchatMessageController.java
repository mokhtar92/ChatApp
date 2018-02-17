/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Message;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Hanaa
 */

public class TempchatMessageController implements Initializable {
@FXML
private VBox vBox;
    Message message;
   
  
    ArrayList<Message> messages=new ArrayList<>();

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
    public void getMessages(Message message){
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
    public void showMessage(Message message){
    Label label=new Label(message.getBody());
        vBox.getChildren().addAll(label);
    }
    
}
