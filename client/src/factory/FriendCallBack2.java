/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import entity.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Hanaa
 */
public class FriendCallBack2 implements Callback<ListView<User>, ListCell<User>>{

    @Override
    public ListCell<User> call(ListView<User> param) {
        return new FriendListCell2();
    }
    
}