/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.User;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Hanaa
 */
public class ServerDbOperation {
     private static Operation operation = null;
    public static ArrayList<User> getUser() throws SQLException{
        operation=new Operation();
        ArrayList<User> users = new ArrayList<>();
       users = operation.getUsers();
       return users;
    }
    public static boolean isFriend(Long userId,Long friendId) throws SQLException{
        return operation.isFriend(userId, friendId);
    }
}
