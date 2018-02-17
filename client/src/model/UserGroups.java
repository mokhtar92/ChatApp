/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.User;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Hanaa
 */
public class UserGroups {
    private static HashMap<String, ArrayList<User>> groups = new HashMap<>();
    private static HashMap<String,String> groupName=new HashMap<>();

    public static HashMap<String, ArrayList<User>> getGroups() {
        return groups;
    }

    public static void setGroups(HashMap<String, ArrayList<User>> groups) {
        UserGroups.groups = groups;
    }

    public static String getGroupName(String id) {
        return groupName.get(id);
    }

    public static void setGroupName(String id,String name) {
        groupName.put(id,name);
    }
    public static void addGroup(String id,ArrayList<User> users){
        groups.put(id, users);
    }
    /*public static void addGroupName(String name){
        groupName.add(name);
    }*/
}
