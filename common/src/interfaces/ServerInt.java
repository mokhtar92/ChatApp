package interfaces;

import entity.FileSender;
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

//    public boolean checkLogin(User user) throws RemoteException;
    public Long checkLogin(User user) throws RemoteException;

    public boolean signUp(User user) throws RemoteException;

    public boolean isEmailExist(String userEmail) throws RemoteException;

    public boolean sendFriendRequest(String email, Long userID) throws RemoteException;

    public ArrayList<User> getRequestedFriend(Long userID) throws RemoteException;

    public ArrayList<User> getAllRequest(Long userID) throws RemoteException;

    public boolean acceptFriendRequest(Long userID, Long friendID) throws RemoteException;

    public boolean deleteFriendRequest(Long receiverID, Long senderID) throws RemoteException;

    public ArrayList<User> getFriendList(Long userId) throws RemoteException;

    public User getUser(String email, String password) throws RemoteException;

    public void ChangeStatus(User user, String Status) throws RemoteException;

    public void tellOthers(entity.Message message) throws RemoteException;

    public void tellOne(entity.Message message) throws RemoteException;

    public void tellgroup(entity.Message message, String group) throws RemoteException;

    public void register(ClientInt client, User user) throws RemoteException;

    public void unregister(ClientInt client, User user) throws RemoteException;

    public void createGroup(ArrayList<User> group, String name) throws RemoteException;

    public int getGroupId() throws RemoteException;

    public String getGroupName(String id) throws RemoteException;

    public User getUserById(long id) throws RemoteException, SQLException;

    public boolean isOnline(String user_id) throws RemoteException;
    public ClientInt getClinetInt(String id)throws RemoteException;

        
}
