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

/**
 *
 * @author Ahmed_Mokhtar
 */
public class StatusListCell extends ListCell<String>{

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty); 
        
        if (item != null) {
            
            Text status = new Text("   " + item);
            ImageView imageView = null;
            
            switch(item){
                case "available":
                    imageView = new ImageView("/resources/available.png");
                    break;
                   
                case "away":
                    imageView = new ImageView("/resources/away.png");
                    break;
                    
                case "busy":
                     imageView = new ImageView("/resources/busy.png");
                     break;
            }
            
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            
            HBox hBox = new HBox(imageView, status); 
            setGraphic(hBox);
            
        } else {
            setGraphic(null);
        }
    }
    
}
