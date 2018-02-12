/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.FileSender;
import entity.Message;
import entity.User;
import java.io.File;
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

/**
 *
 * @author Hanaa
 */
public class ServerImpl extends UnicastRemoteObject implements ServerInt{
    
    private static HashMap<String, ClientInt> clients = new HashMap<>();
    private HashMap<String, ArrayList<String>> groups = new HashMap<>();
    private HashMap<String,FileSender> files = new HashMap<>();
    static int group_Id = 0;
    static int file_Id = 0;
    ArrayList<Message> arrayList = new ArrayList<>();
    static Registry registry = null;
    static {
        try {
            registry=LocateRegistry.createRegistry(2000);
        } catch (RemoteException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ServerImpl() throws RemoteException {
      
    }
    
    public static void startSer() {
        try {
            Operation op=new Operation();
            op.getUsers();
            
            registry.rebind("chat", new ServerImpl());
            
        } catch (RemoteException ex) {
            
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public static void stop() throws RemoteException, NotBoundException {
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
        ClientInt client = clients.get(message.getFrom());
        if (clients.containsKey(message.getTo())) {
            client.recieve(message);
            client = clients.get(message.getTo());
            client.recieve(message);
        }
    }
    
    @Override
    public void tellgroup(Message message, String group) throws RemoteException {
        ArrayList<String> selectedGroup = groups.get(group);
        for (String id : selectedGroup) {
            if (clients.containsKey(id)) {
                ClientInt client = clients.get(id);
                client.recieve(message);
            }
        }
    }
    
    @Override
    public void register(ClientInt client, User user) throws RemoteException {
        
        clients.put(user.getRecId() + "", client);
    }
    
    @Override
    public void unregister(ClientInt client) throws RemoteException {
        clients.remove(client);
    }
    
    @Override
    public void createGroup(ArrayList<String> group) throws RemoteException {
        group_Id++;
        groups.put(group_Id + "", group);
    }
    @Override
   public void sendFile(FileSender fileSender)throws RemoteException{
       file_Id++;
       files.put(file_Id+"", fileSender);
        System.out.println(files.size());
        Message message=fileSender.getMessage();
        message.setBody(fileSender.getFile().toString());
         ClientInt client = clients.get(message.getFrom());
        if (clients.containsKey(message.getTo())) {
             client = clients.get(message.getTo());
            client.sendFileToReciever(fileSender);
           
          
            
        }
    }
    @Override
    public boolean checkLogin(User user) throws RemoteException {
        boolean isValid = false;
        PreparedStatement ps = Database.getInstance().getPreparedStatement("SELECT * FROM ITI_CHATAPP_USER WHERE email=? AND password=?");
        try {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isValid = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Database.getInstance().release();
        }
        return isValid;
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
            PreparedStatement ps = Database.getInstance().getPreparedStatement("INSERT INTO ITI_CHATAPP_USER (firstName,lastName,password,email,country,birthdate,Gender,imgURL) VALUES (?,?,?,?,?,?,?,?)");
            try {
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getEmail());
                ps.setString(5, user.getCountry());
                ps.setString(8,user.getImgURL());
                ps.setDate(6, new java.sql.Date(user.getBirthDate().getTime()));
                ps.setString(7, user.getGender());
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
}
