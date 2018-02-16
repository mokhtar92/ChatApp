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
    private static ArrayList<String> groupName=new ArrayList<>();

    public static HashMap<String, ArrayList<User>> getGroups() {
        return groups;
    }

    public static void setGroups(HashMap<String, ArrayList<User>> groups) {
        UserGroups.groups = groups;
    }

    public static ArrayList<String> getGroupName() {
        return groupName;
    }

    public static void setGroupName(ArrayList<String> groupName) {
        UserGroups.groupName = groupName;
    }
    public static void addGroup(String id,ArrayList<User> users){
        groups.put(id, users);
    }
    public static void addGroupName(String name){
        groupName.add(name);
    }
}
