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
import java.util.logging.Level;
import java.util.logging.Logger;

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
            user.setBirthDate(resultSet.getDate(7));
            user.setGender(resultSet.getString(8));
            user.setMyStatus(resultSet.getString(9));
            users.add(user);
        }

        return users;

    }

    ;

 public boolean isFriend(Long userId, Long friendId) throws SQLException {
        Database db = Database.getInstance();
        String query = "select * from ITI_CHATAPP_FRIENDLIST where (userid='" + userId + "'and friendid='" + friendId + "')or(userid='" + friendId + "'and friendid='" + userId + "')";
        PreparedStatement preparedStatement = db.getPreparedStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return true;
        }
        return false;
    }

    public User getUserById(Long id) throws SQLException {
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

    public Long getUserIdByEmail(String email) {
        Long friendID = -1L;
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT recid FROM ITI_CHATAPP_USER WHERE email=?");
        try {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                friendID = rs.getLong("recid");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return friendID;
    }

    public int maleFemale(String gender) {
        int result = 0;
        try {
            Statement stmt = Database.getInstance().getPreparedStatementUpdatable(gender);
            String query;

            query = "select count (gender) from ITI_CHATAPP_USER where gender = '" + gender + "'";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                //result = rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3);
                result = rs.getInt(1);
            }

            return result;
            //  con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    //maroof
    public int getAllUser() {
        int result = 0;
        try {

            String query;

            query = "select count(RECID) from ITI_CHATAPP_USER";
            Statement stmt = Database.getInstance().getPreparedStatementUpdatable(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                result = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
