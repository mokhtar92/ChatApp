/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


/**
 *
 * @author Ahmed_Mokhtar
 */
public class AvatarCallback implements Callback<ListView<String>, ListCell<String>>{

    @Override
    public ListCell<String> call(ListView<String> param) {
        return new AvatarListCell();
    }
}
