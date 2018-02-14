/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hanaa
 */
public class Operation {
    
    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        Database db = Database.getInstance();
        String query = "select * from ITI_CHATAPP_USER";
        PreparedStatement preparedStatement = db.getPreparedStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            User user = new User();
            user.setRecId(resultSet.getLong(1));
            user.setFirstName(resultSet.getString(2));
            user.setLastName(resultSet.getString(3));
            user.setEmail(resultSet.getString(4));
            user.setPassword(resultSet.getString(5));
            user.setCountry(resultSet.getString(6));
//            user.setBirthDate(new Date(resultSet.getString(7)));
            user.setGender(resultSet.getString(8));
            user.setMyStatus(resultSet.getString(9));
            users.add(user);
        }

        return users;

    }
    
    public int maleFemale(String gender) throws SQLException
    
    {
       int Count = 0;
        Database db = Database.getInstance();
        String query = "select count(RECID) from ITI_CHATAPP_USER where gender like '"+gender+"'";
        PreparedStatement preparedStatement = db.getPreparedStatementUpdatable(query);
        ResultSet set = preparedStatement.executeQuery();
        while(set.next())
        {
           Count=set.getInt(1);
        }
        return Count;
    }
    
    
  public void ChangeStatus(User user,String Status) throws SQLException{
        String query="update ITI_CHATAPP_USER set myStatus='"+Status+"' where Recid='"+user.getRecId()+"'";
        Database db=Database.getInstance();
        PreparedStatement preparedStatement =db.getPreparedStatement(query);
        ResultSet resultSet=preparedStatement.executeQuery();
  }
}
