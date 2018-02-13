/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.User;

/**
 *
 * @author Hanaa
 */
public class UserSession {
    private static User user=null;
    public UserSession(User user){
    this.user=user;
    }

    /**
     * @return the user
     */
    public static User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
}
