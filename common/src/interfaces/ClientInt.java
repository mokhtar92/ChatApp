package interfaces;

import entity.FileSender;
import entity.Message;
import entity.User;
import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public interface ClientInt extends Remote {

    public void recieve(Message message, String group) throws RemoteException;

    public void recieveAnnoncement(String message) throws RemoteException;

    public void recieveNotification(int status, User user) throws RemoteException;
    
    public void recieveFileNotification(int status, User user) throws RemoteException;

    public void requestNotification(int status, User user) throws RemoteException;

    public void reciveFile(String path, String filename, boolean append, byte[] data, int dataLength) throws RemoteException;

    public void sendFileToReciever(FileSender file) throws RemoteException;

}
