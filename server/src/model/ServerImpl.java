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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private static HashMap<String, ClientInt> users = new HashMap<>();
    private HashMap<String, ArrayList<User>> groups = new HashMap<>();
    private HashMap<String, String> groupsName = new HashMap<>();
    private HashMap<String, FileSender> files = new HashMap<>();
    static FXMLServerScreenController controller = null;

    public static void setController(FXMLServerScreenController c) {
        controller = c;
    }
    static int group_Id = 0;
    static int file_Id = 0;
    private static boolean serverFlag = false;
    ArrayList<Message> arrayList = new ArrayList<>();

    public static boolean isServerFlag() {
        return serverFlag;
    }

    public static void setServerFlag(boolean serverFlag1) {
        serverFlag = serverFlag1;
    }
    private static Registry registry = null;

    static {
        try {
            registry = LocateRegistry.createRegistry(2000);
        } catch (RemoteException ex) {
           Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("can't load");
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
        sendAnnouncement("###!!!");
        clients=new HashMap<>();
        users=new HashMap<>();
        if(!isServerFlag()){
        registry.unbind("chat");
        }
    }

    @Override
    public void tellOthers(Message message) throws RemoteException {

        for (int i = 0; i < clients.size(); i++) {
            ClientInt client = clients.get(i);
            client.recieve(message, null);
        }
    }

    @Override
    public void tellOne(Message message) throws RemoteException {

        if (users.containsKey(message.getTo().get(0))) {
            ClientInt client = users.get(message.getTo().get(0));
            client.recieve(message, null);
            client = users.get(message.getFrom());
            client.recieve(message, null);

        }

    }

    @Override
    public void tellgroup(Message message, String group) throws RemoteException {
        ArrayList<User> selectedGroup = groups.get(group);
        for (User user : selectedGroup) {
            if (users.containsKey(user.getRecId() + "")) {
                ClientInt client = users.get(user.getRecId() + "");
                client.recieve(message, group);

            }
        }
    }

    @Override
    public void register(ClientInt client, User user) throws RemoteException {
        boolean flag = false;
        Iterator it = users.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            String id = (String) pair.getKey();
            ClientInt iteratorclient = (ClientInt) pair.getValue();
            if (iteratorclient != null) {
                if(id.equals(user.getRecId()+"")){
                    flag=true;
                }
            }
        }
        if(!flag){
        clients.put(user, client);
        users.put(user.getRecId() + "", client);
        try {
            notifyFriends(user);
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }

    @Override
    public void unregister(ClientInt client, User user) throws RemoteException {
        users.remove(user.getRecId());
        Iterator it = clients.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            ClientInt iteratorClient = (ClientInt) pair.getValue();
            if (client != null) {
                if (client.equals(iteratorClient)) {
                    it.remove();
                }
            }
        }
        it = users.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            ClientInt iteratorClient = (ClientInt) pair.getValue();
            if (client != null) {
                if (client.equals(iteratorClient)) {
                    it.remove();
                }
            }
        }

    }

    @Override
    public void createGroup(ArrayList<User> group, String name) throws RemoteException {
        group_Id++;
        groups.put("group" + group_Id, group);
        groupsName.put("group" + group_Id, name);
    }

    @Override
    public void sendFile(FileSender fileSender) throws RemoteException {
        file_Id++;
        files.put(file_Id + "", fileSender);
        Message message = fileSender.getMessage();
//        message.setBody(fileSender.getFile().getName().toString());
        ClientInt client = users.get(message.getFrom());
        User user = null;
        try {
            user = getUserById(Long.parseLong(message.getFrom()));
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (users.containsKey(message.getTo().get(0))) {
            client = users.get(message.getTo().get(0));
            client.sendFileToReciever(fileSender);
            client.recieveFileNotification(NotificationStatus.fileSendStatus, user);
        }
    }

    @Override
    public int getGroupId() throws RemoteException {
        return group_Id;
    }

    @Override
    public String getGroupName(String id) throws RemoteException {
        return groupsName.get(id);
    }

    public static void sendAnnouncement(String message) throws RemoteException {

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
        if (isEmailExist(user.getEmail())) {
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
        } else {
            userID = 0L;
        }

        return userID;
    }

    @Override
    public boolean isEmailExist(String userEmail) throws RemoteException {
        boolean isExist = false;
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT recid FROM ITI_CHATAPP_USER WHERE email=?");
        try {
            ps.setString(1, userEmail);
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
        if (!isEmailExist(user.getEmail())) {
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
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT u.* "
                + "FROM ITI_CHATAPP_USER u , ITI_CHATAPP_FRIENDLIST fl "
                + "WHERE ( U.RECID != ? AND ( U.RECID = FL.FRIENDID  OR U.RECID = FL.USERID )) "
                + "AND ( FL.FRIENDID = ? OR FL.USERID = ? )");
        try {
            ps.setLong(1, userId);
            ps.setLong(2, userId);
            ps.setLong(3, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setRecId(rs.getLong("recid"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setCountry(rs.getString("COUNTRY"));
//                user.setBirthDate(rs.getDate("BIRTHDATE"));
                user.setGender(rs.getString("GENDER"));
                user.setImgURL(rs.getString("imgURL"));
                user.setMyStatus(rs.getString("MYSTATUS"));
                list.add(user);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return list;

    }

    /**
     * ********************* Send Request ***********************
     */
    @Override
    public boolean sendFriendRequest(String email, Long userID) throws RemoteException {
        Long friendID = opr.getUserIdByEmail(email);
        boolean isSend = false;
        User user = new User();
        PreparedStatement ps = Database.getInstance().getPreparedStatement("INSERT INTO ITI_CHATAPP_FRIENDREQUEST (SENDERID, RECEIVERID) VALUES(?,?)");
        try {
            ps.setLong(1, userID);
            ps.setLong(2, friendID);
            int rowsEffected = ps.executeUpdate();
            if (rowsEffected == 1) {
                isSend = true;
                user = opr.getUserById(friendID);
                requestNotification(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return isSend;
    }

    /**
     * ************************ all sent requests by this user
     * ******************
     */
    @Override
    public ArrayList<User> getRequestedFriend(Long userID) {
        ArrayList<User> requestSend = new ArrayList<>();
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT * FROM  ITI_CHATAPP_USER WHERE recid IN ( SELECT RECEIVERID FROM ITI_CHATAPP_FRIENDREQUEST WHERE SENDERID = ?)");
        try {
            ps.setLong(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setRecId(rs.getLong("recid"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setImgURL(rs.getString("imgURL"));
                requestSend.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return requestSend;
    }

    /**
     * *********************** Show All requests ***********************
     */
    @Override
    public ArrayList<User> getAllRequest(Long userID) {
        ArrayList<User> allRequest = new ArrayList<>();
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT * FROM  ITI_CHATAPP_USER WHERE recid IN ( SELECT SENDERID FROM ITI_CHATAPP_FRIENDREQUEST WHERE RECEIVERID = ?)");
        try {
            ps.setLong(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setRecId(rs.getLong("recid"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setImgURL(rs.getString("imgURL"));
                allRequest.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return allRequest;
    }

    /**
     * *********** Accept friend request**************
     */
    @Override
    public boolean acceptFriendRequest(Long userID, Long friendID) throws RemoteException {
        boolean isAccepted = false;
        PreparedStatement ps = Database.getInstance().getPreparedStatement("INSERT INTO ITI_CHATAPP_FRIENDLIST FL (FL.FRIENDID, FL.USERID) VALUES (?,?)");
        try {
            ps.setLong(1, friendID);
            ps.setLong(2, userID);
            int rowsEffected = ps.executeUpdate();
            if (rowsEffected != 0) {
                isAccepted = true;
                deleteFriendRequest(userID, friendID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return isAccepted;
    }

    /**
     * ********* Ignore friend Request ****************
     */
    @Override
    public boolean deleteFriendRequest(Long receiverID, Long senderID) throws RemoteException {
        boolean isDeleted = false;
        PreparedStatement ps = Database.getInstance().getPreparedStatement("DELETE FROM ITI_CHATAPP_FRIENDREQUEST FR WHERE FR.SENDERID = ? AND FR.RECEIVERID = ?");
        try {
            ps.setLong(1, senderID);
            ps.setLong(2, receiverID);
            if (ps.executeUpdate() > 0) {
                isDeleted = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return isDeleted;
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

    public void requestNotification(User user) throws RemoteException, SQLException {
        Iterator it = clients.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            User friend = (User) pair.getKey();
            ClientInt client = (ClientInt) pair.getValue();
            if (client != null) {
                if (user.getRecId().equals(friend.getRecId())) {
                    client.requestNotification(NotificationStatus.friendRequest, user);
                }
            }
        }
    }

    public void notifyFriends(User user) throws RemoteException, SQLException {
        Iterator it = clients.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            User friend = (User) pair.getKey();
            ClientInt client = (ClientInt) pair.getValue();
            if (client != null) {
                if (ServerDbOperation.isFriend(user.getRecId(), friend.getRecId())) {
                    client.recieveNotification(NotificationStatus.onlineStatus, user);
                }
            }
        }

    }

    @Override
    public User getUserById(long id) throws RemoteException, SQLException {
        return opr.getUserById(id);

    }

    //maroof
    public int GetClientcount() {
        return clients.size();
    }

    @Override
    public boolean isOnline(String user_id) throws RemoteException {
        boolean flag = false;

        Iterator it = users.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            String id = (String) pair.getKey();
            ClientInt client = (ClientInt) pair.getValue();
            if (client != null) {
                if (id.equals(user_id)) {
                    flag = true;
                }
            }
        }
        return flag;
    }
    @Override
    public void reciveFile(FileSender fileSender) {
      /*  try {
            String [] split = fileSender.getPath().split("\\.(?=[^\\.]+$)");
             File file =null ; 
            if(split.length <2){
                split = fileSender.getFileName().split("\\.(?=[^\\.]+$)");
                String extension = "."+split[1];
                file = new File(fileSender.getPath()+extension);
            }else{
                file = new File(fileSender.getPath());
            }
            

            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, fileSender.isAppend());
            out.write(fileSender.getData(), 0, fileSender.getDataLength());
            out.flush();
            out.close();
            Message message=fileSender.getMessage();
           // fileSender.setPath("F:\\"+file.getName());
            ClientInt client = users.get(message.getFrom());
        User user = null;
        try {
            user = getUserById(Long.parseLong(message.getFrom()));
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (users.containsKey(message.getTo().get(0))) {
            client = users.get(message.getTo().get(0));
            client.sendFileToReciever(fileSender);
            client.recieveFileNotification(NotificationStatus.fileSendStatus, user);
        }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
*/
    }
    @Override
    public ClientInt getClinetInt(String id)throws RemoteException{
       return users.get(id);
    
    }
}
