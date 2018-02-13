package interfaces;

import entity.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Ghada
 */
public interface ServerInt extends Remote {

    public boolean checkLogin(User user) throws RemoteException;

    public boolean signUp(User user) throws RemoteException;

    public User getUser(String email, String password) throws RemoteException;

    public void ChangeStatus(User user, String Status) throws RemoteException;

    public void tellOthers(entity.Message message) throws RemoteException;

    public void tellOne(entity.Message message) throws RemoteException;

    public void tellgroup(entity.Message message, String group) throws RemoteException;

    public void register(ClientInt client, User user) throws RemoteException;

    public void unregister(ClientInt client) throws RemoteException;

    public void createGroup(ArrayList<String> group) throws RemoteException;

    public void sendFile(entity.FileSender file) throws RemoteException;
    //public void notifyFriends(boolean status) throws RemoteException;
}
