/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import entity.User;


/**
 *
 * @author Ahmed_Mokhtar
 */
public class RequestCallback implements Callback<ListView<User>, ListCell<User>>{

    @Override
    public ListCell<User> call(ListView<User> param) {
        return new RequestListCell();
    }    
}
