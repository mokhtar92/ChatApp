/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.FXMLServerScreenController;
import entity.FileSender;
import entity.Message;
import entity.NotificationStatus;
import entity.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import interfaces.ClientInt;
import interfaces.ServerInt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 *
 * @author Hanaa
 */
public class ServerImpl extends UnicastRemoteObject implements ServerInt {


    Operation opr = new Operation();
    private static HashMap<User, ClientInt> clients = new HashMap<>();
    private HashMap<String, ArrayList<User>> groups = new HashMap<>();
    private HashMap<String, FileSender> files = new HashMap<>();
    static FXMLServerScreenController controller=null;

    public static void  setController(FXMLServerScreenController c) {
        controller = c;
    }
    static int group_Id = 0;
    static int file_Id = 0;
    private static boolean serverFlag = false;
    ArrayList<Message> arrayList = new ArrayList<>();

    public static boolean isServerFlag() {
        return serverFlag;
    }

    public static void setServerFlag(boolean serverFlag) {
        serverFlag = serverFlag;
    }
    private static Registry registry = null;

    static {
        try {
            registry = LocateRegistry.createRegistry(2000);
        } catch (RemoteException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ServerImpl() throws RemoteException {

    }

    public static void startServer() {
        try {
            Operation op = new Operation();
            op.getUsers();
            serverFlag = true;
            registry.rebind("chat", new ServerImpl());

        } catch (RemoteException ex) {

            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void stopServer() throws RemoteException, NotBoundException {
        registry.unbind("chat");
    }

    @Override
    public void tellOthers(Message message) throws RemoteException {

        for (int i = 0; i < clients.size(); i++) {
            ClientInt client = clients.get(i);
            client.recieve(message);
        }
    }

    @Override
    public void tellOne(Message message) throws RemoteException {
        ClientInt client = clients.get(message.getTo());
        if (clients.containsKey(message.getTo())) {
            client.recieve(message);
            client = clients.get(message.getFrom());
            client.recieve(message);
        }
    }

    @Override
    public void tellgroup(Message message, String group) throws RemoteException {
        ArrayList<User> selectedGroup = groups.get(group);
        for (User user : selectedGroup) {
            if (clients.containsKey(user.getRecId())) {
                ClientInt client = clients.get(user.getRecId());
                client.recieve(message);
            }
        }
    }

    @Override
    public void register(ClientInt client, User user) throws RemoteException {

        clients.put(user, client);
        try {
            notifyFriends(user);
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void unregister(ClientInt client,User user) throws RemoteException {
        clients.remove(client);
    }

    @Override
    public void createGroup(ArrayList<User> group) throws RemoteException {
        group_Id++;
        groups.put(group_Id + "", group);
        System.out.println("group created"+groups.size());
    }

    @Override
    public void sendFile(FileSender fileSender) throws RemoteException {
        file_Id++;
        files.put(file_Id + "", fileSender);
        System.out.println(files.size());
        Message message = fileSender.getMessage();
        message.setBody(fileSender.getFile().toString());
        ClientInt client = clients.get(message.getFrom());
        if (clients.containsKey(message.getTo())) {
            client = clients.get(message.getTo());
            client.sendFileToReciever(fileSender);

        }
    }
    @Override
     public int getGroupId(ArrayList<User> group) throws RemoteException{
         return group_Id;
     }

    public static void sendAnnoncement(String message) throws RemoteException {

        Iterator it = clients.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            ClientInt client = (ClientInt) pair.getValue();
            if (client != null) {
                client.recieveAnnoncement(message);
            }
        }

    }

    @Override
    public Long checkLogin(User user) throws RemoteException {
        Long userID = 0L;
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT * FROM ITI_CHATAPP_USER WHERE email=? AND password=?");
        try {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userID = rs.getLong("recId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Database.getInstance().release();
        }
        return userID;
    }

    private boolean checkEmailExist(User user) {
        boolean isExist = false;
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT recid FROM ITI_CHATAPP_USER WHERE email=?");
        try {
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isExist = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return isExist;
    }

    @Override
    public boolean signUp(User user) throws RemoteException {
        boolean storedFlag = false;
        if (!checkEmailExist(user)) {
            PreparedStatement ps = Database.getInstance().getPreparedStatement("INSERT INTO ITI_CHATAPP_USER (firstName,lastName,password,email,country,birthdate,Gender,imgURL, myStatus) VALUES (?,?,?,?,?,?,?,?,?)");
            try {
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getEmail());
                ps.setString(5, user.getCountry());
                ps.setDate(6, new java.sql.Date(user.getBirthDate().getTime()));
                ps.setString(7, user.getGender());
                ps.setString(8, user.getImgURL());
                user.setMyStatus("available");
                ps.setString(9, user.getMyStatus());
                
                int rowsEffected = ps.executeUpdate();
                if (rowsEffected == 1) {
                    storedFlag = true;
                   
                    controller.data.add(user);
                    //controller.usersTable.setItems(controller.data);
                    controller.updateTabelView();
                  
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            } finally {
                Database.getInstance().release();
            }
        }
        return storedFlag;
    }

    @Override
    public ArrayList<User> getFriendList(Long userId) {
        ArrayList<User> list = new ArrayList<>();
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT FRIENDID FROM ITI_CHATAPP_FRIENDLIST WHERE USERID=?");
        try {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            boolean flag=rs.next();
            if(flag){
            while (flag) {
                User user = opr.getUserById(rs.getLong("FRIENDID"));
                list.add(user);
                flag=rs.next();
            }
            }else{
          /* PreparedStatement ps2 = Database.getInstance().getPreparedStatement("SELECT USERID FROM ITI_CHATAPP_FRIENDLIST WHERE FRIENDID=?");
             ps2.setLong(1, userId);
             rs = ps2.executeQuery();
             flag=rs.next();
             while (flag) {
                User user = opr.getUserById(rs.getLong("USERID"));
                list.add(user);
                flag=rs.next();
            }*/
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return list;

    }

    public User getUser(String email, String password) throws RemoteException {

        String query = "select * from ITI_CHATAPP_USER where email='" + email + "' and password='" + password + "'";
        PreparedStatement preparedStatement = Database.getInstance().getPreparedStatement(query);
        ResultSet resultSet;
        try {
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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
            user.setImgURL(resultSet.getString(10));

                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public void ChangeStatus(User user, String Status) throws RemoteException {
        String query = "update ITI_CHATAPP_USER set myStatus='" + Status + "' where Recid='" + user.getRecId() + "'";
        Database db = Database.getInstance();
        PreparedStatement preparedStatement = db.getPreparedStatement(query);
        try {
            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void notifyFriends(User user) throws RemoteException, SQLException{
           Iterator it = clients.entrySet().iterator();
             while (it.hasNext()) {
                 HashMap.Entry pair = (HashMap.Entry)it.next();
                 User friend= (User)pair.getKey();
                 ClientInt client= (ClientInt)pair.getValue();
                if(client!=null){
                    if(ServerDbOperation.isFriend(user.getRecId(),friend.getRecId())){
                        client.recieveNotification(NotificationStatus.onlineStatus,user);
                    }
             }
            }
         
     }
}
