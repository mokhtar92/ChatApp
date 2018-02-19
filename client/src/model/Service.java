package model;

import entity.FileSender;
import entity.Message;
import entity.User;
import interfaces.ClientInt;
import interfaces.ServerInt;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Ghada
 */
public class Service {

    static Long userID = null;
    //maroof edit
    static public String IP = null;

    public Long checkLogin(User user) {
        try {
            userID = getServer().checkLogin(user);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return userID;
    }

    public boolean isEmailExist(String userEmail) {
        boolean isExist = false;
        try {
            isExist = getServer().isEmailExist(userEmail);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return isExist;
    }

    public boolean signUp(User user) {
        boolean isEffected = false;
        try {
            isEffected = getServer().signUp(user);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return isEffected;
    }

    public boolean sendFriendRequest(String email) {
        boolean isSend = false;
        try {
            isSend = getServer().sendFriendRequest(email, userID);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return isSend;
    }

    public ArrayList<User> getRequestedFriend() {
        ArrayList<User> myRequestedlist = new ArrayList<>();
        try {
            myRequestedlist = getServer().getRequestedFriend(userID);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return myRequestedlist;
    }

    public ArrayList<User> getAllRequest() {
        ArrayList<User> allRequest = new ArrayList<>();
        try {
            allRequest = getServer().getAllRequest(userID);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return allRequest;
    }

    public boolean acceptFriendRequest(Long friendID) {
        boolean isAccepted = false;
        try {
            isAccepted = getServer().acceptFriendRequest(userID, friendID);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return isAccepted;
    }

    public boolean deleteFriendRequest(Long senderID) {
        boolean isDeleted = false;
        try {
            isDeleted = getServer().deleteFriendRequest(userID, senderID);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return isDeleted;
    }

    public ArrayList<User> getFriendList() {
        ArrayList<User> list = new ArrayList<>();
        try {
            list = getServer().getFriendList(userID);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return list;
    }

    public static User getUser(String email, String password) throws RemoteException {

        User user = getServer().getUser(email, password);
        return user;
    }

    public static ServerInt getServer() {
        ServerInt server = null;
        try {
            //maroof edit here
            Registry reg = LocateRegistry.getRegistry(IP, 2000);
            server = (ServerInt) reg.lookup("chat");
        } catch (NotBoundException | RemoteException ex) {
           
        }
        return server;
    }

    public static void register(ClientInt client, User user) throws RemoteException {
        getServer().register(client, user);
    }

    public static void Unregister(ClientInt client, User user) throws RemoteException {
        getServer().unregister(client, user);
    }

    public static void createGroup(ArrayList<User> group, String name) throws RemoteException {
        getServer().createGroup(group, name);
    }

    public static int getGroupId() throws RemoteException {
        return getServer().getGroupId();
    }

    public static String getGroupName(String id) throws RemoteException {
        return getServer().getGroupName(id);
    }

    public static void tellOne(Message message) throws RemoteException {
        getServer().tellOne(message);
    }

    public static void tellGroup(Message message, String group) throws RemoteException {
        getServer().tellgroup(message, group);
    }

    public static User getUserById(long id) throws RemoteException, SQLException {
        return getServer().getUserById(id);
    }
    public static void sendFile(FileSender fileSender) throws RemoteException{
        getServer().sendFile(fileSender);
    }
     public static void sendFile2(FileSender fileSender) throws RemoteException{
        getServer().reciveFile(fileSender);
    }
    public static boolean isOnline(String id) throws RemoteException{
    return getServer().isOnline(id);
    }
     public static ClientInt getClinetInt(String id)throws RemoteException{
     
         return getServer().getClinetInt(id);
     }
    //maroof adding for check ip 
    public static int checkIp ()
    {
       if(getServer()==null)
       {
         return 0;
       }
       else
       {
        return 1;
       }
    }
}
