package model;

import entity.Message;
import entity.User;
import interfaces.ClientInt;
import interfaces.ServerInt;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Ghada
 */
public class Service {
    public boolean checkLogin(User user){
        boolean isValid = false;
        try {
            isValid = getServer().checkLogin(user);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return isValid;
    }
    
    public boolean signUp (User user){
        boolean isEffected = false;
        try {
            isEffected = getServer().signUp(user);
        } catch (RemoteException ex) {
            ex.printStackTrace(System.out);
        }
        return isEffected;
    }
    public static User getUser(String email,String password) throws RemoteException{
        
            User user=getServer().getUser(email, password);
            return user;
    }
    public static ServerInt getServer(){
        ServerInt server = null;
        try{
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 2000);
            server = (ServerInt) reg.lookup("chat");
        }catch(NotBoundException | RemoteException ex){
            ex.printStackTrace(System.out);
        }
        return server;
    }
    public static void register(ClientInt client,User user) throws RemoteException{
        getServer().register(client, user);
    }
    public static void tellOne(Message message) throws RemoteException{
        getServer().tellOne(message);
    }
    
}
