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
import java.util.ArrayList;


/**
 *
 * @author Hanaa
 */
public class Operation {
    public ArrayList<User> getUsers() throws SQLException{
        ArrayList<User> users=new ArrayList<>();
        Database db=Database.getInstance();
        String query="select * from ITI_CHATAPP_USER";
        PreparedStatement preparedStatement =db.getPreparedStatement(query);
        ResultSet resultSet=preparedStatement.executeQuery();
        while(resultSet.next()) {            
            User user=new User();
            user.setRecId(resultSet.getLong(1));
            user.setFirstName(resultSet.getString(2));
            user.setLastName(resultSet.getString(3));
            user.setEmail(resultSet.getString(4));
            user.setPassword(resultSet.getString(5));
            user.setCountry(resultSet.getString(6));
            user.setBirthDate(resultSet.getDate(7));
            user.setGender(resultSet.getString(8));
            user.setMyStatus(resultSet.getString(9));
            users.add(user);
        }
        
        return users;
       
};
    
        public User getUserById(Long id) throws SQLException{
        User user = new User();
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT * FROM ITI_CHATAPP_USER WHERE recid=?");
        try {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setRecId(rs.getLong("recId"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("lastname"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setCountry(rs.getString("COUNTRY"));
                user.setBirthDate(rs.getDate("BIRTHDATE"));
                user.setGender(rs.getString("GENDER"));
                user.setMyStatus(rs.getString("MYSTATUS"));
                user.setImgURL(rs.getString("IMGURL"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return user;
    }
 
}
