/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import controller.FXMLChatScreenController;
import entity.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FriendCallback implements Callback<ListView<User>, ListCell<User>>{
    
    FXMLChatScreenController controller;
    boolean status;

    public FriendCallback(FXMLChatScreenController aThis,boolean status) {
        controller = aThis;
        this.status=status;
    }

    public FriendCallback(boolean status) {
        this.status=status;
    
    }

    @Override
    public ListCell<User> call(ListView<User> param) {
        return new FriendListCell(controller,status);
    }
    
}
