/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FriendListCell extends ListCell<String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        
        if (item != null) {
            
            ImageView userImg = new ImageView("/resources/man.png");
            userImg.setFitWidth(80);
            userImg.setFitHeight(80);
            
            ImageView statusImg = new ImageView("/resources/online.png");
            statusImg.setFitWidth(25);
            statusImg.setFitHeight(25);
           
            
            Text username = new Text("  " + item + "  ");
            
            HBox hBox = new HBox(userImg, username, statusImg);
            
            hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("friend item is pressed");
                }
            });
            
            setGraphic(hBox);
            
        } else {
            setGraphic(null);
        }
    }
    
    
}
