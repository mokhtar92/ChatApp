/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import interfaces.NotificationInt;
import java.io.Serializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Hanaa
 */
public class NotificationImpl implements NotificationInt,Serializable{

    @Override
    public void createNotification(String title, String content, String ImgUrl) {
          //notification code here 
        Image img= new Image(ImgUrl);
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(75);
        imgView.setFitWidth(75);
        
        Notifications notification = Notifications.create()
                .hideAfter(Duration.seconds(20))
                .position(Pos.BOTTOM_RIGHT)
                .text(content)
                .title(title)
                .graphic(imgView)
                ;
        
         notification.show();
    }
    
}
