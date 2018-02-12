/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

;
import entity.FileSender;
import entity.Message;
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

    public ClientImpl() throws RemoteException {
        
    }

    @Override
  public void recieve(Message message)throws RemoteException {
        //chat.render(message);
        
    }
  
  @Override
    public void reciveFile(String path, String filename,boolean append, byte[] data, int dataLength) {

        try {
            File f  ; 
            
            String [] split = path.split("\\.(?=[^\\.]+$)");
            //handle not write extesion 
            if(split.length <2){
                split = filename.split("\\.(?=[^\\.]+$)");
                String extension = "."+split[1];
                f = new File(path+extension);
            }else{
                f = new File(path);
            }
            

            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f, append);
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
