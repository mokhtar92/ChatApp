/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.FXMLServerScreenController;
import entity.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author Hanaa
 */
public class UserTable {
   private TableView<User> usersTable;
    ObservableList<User> data;

    public UserTable(TableView<User> userTable) {
        this.usersTable=usersTable;
        
    }

    public TableView<User> getUsersTable() {
        return usersTable;
    }

    public void setUsersTable(TableView<User> usersTable) {
        this.usersTable = usersTable;
        
    }

    public ObservableList<User> getData() {
        fillTable();
        return data;
    }

    public void setData(ObservableList<User> data) {
        this.data = data;
    }
    public void fillTable(){
        try {

            ArrayList<User> users = new ArrayList<>();
            
            users=ServerDbOperation.getUser();
            this.data = FXCollections.observableArrayList();
            for (User user : users) {
                data.add(user);

            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLServerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
