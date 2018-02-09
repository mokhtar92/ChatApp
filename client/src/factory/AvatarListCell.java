/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;


/**
 *
 * @author Ahmed_Mokhtar
 */
public class AvatarListCell extends ListCell<String>{

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty); 
        
        if(item!=null){
            ImageView imageView = new ImageView(item);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            setGraphic(imageView);
        } else {
            setGraphic(null);
        }
    }
}
