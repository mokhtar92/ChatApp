/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

;
import controller.FXMLChatScreenController;
import entity.FileSender;
import entity.Message;
import entity.User;
import interfaces.ClientInt;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ClientImpl extends UnicastRemoteObject implements ClientInt {

   
    static int file_Id = 0;
    public static HashMap<String,FileSender> files = new HashMap<>();
    FXMLChatScreenController chat;
    public ClientImpl() throws RemoteException {
       
    }
    public ClientImpl(FXMLChatScreenController chat) throws RemoteException {
        this.chat=chat;
    }

    @Override
  public void recieve(Message message,String group)throws RemoteException {
      
        chat.getMessage(message,group);
        
        
    }
  @Override
  public void recieveAnnoncement(String message)throws RemoteException{
  
      chat.getAnnoncement(message);
  }
  @Override
  public void recieveNotification(int status,User user)throws RemoteException{
      chat.getNotification(status,user);
  }
   @Override
  public void requestNotification(int status,User user)throws RemoteException{
      chat.requestNotification(status,user);
  }
  @Override
    public void reciveFile(String path, String filename,boolean append, byte[] data, int dataLength) {

        try {
            String [] split = path.split("\\.(?=[^\\.]+$)");
             File file =null ; 
            if(split.length <2){
                split = filename.split("\\.(?=[^\\.]+$)");
                String extension = "."+split[1];
                file = new File(path+extension);
            }else{
                file = new File(path);
            }
            

            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, append);
            out.write(data, 0, dataLength);
            out.flush();
            out.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    @Override
     public void sendFileToReciever(FileSender fileSender)throws RemoteException{
         
     file_Id++;
       files.put(file_Id+"", fileSender);
     }
}
