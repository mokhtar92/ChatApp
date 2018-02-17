/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Ahmed
 */
public class XmlHandler {
    
    public  void SaveXml (File file , List<Message> messages) throws JAXBException, FileNotFoundException, IOException
    {
    
        //create JAXB object to write 
         JAXBContext context = JAXBContext.newInstance("entity");
        
         ObjectFactory factory = new ObjectFactory();
        //create object from chatClass and get message Array out 
         Chat newChat =factory.createChat(); 
         List<Message> messageCollection = newChat.getMessage();
         
         //loop for all passed messages to create Xml out of it and pass to messageCollection 
         for (Message m : messages)
         {
             //temp refrence to loop and put in it 
            Message tempMessage = factory.createMessage();
            
            //fill data in temp obj
            tempMessage.setFrom(m.getFrom());
            tempMessage.getTo().addAll(m.getTo());
            tempMessage.setBody(m.getBody());
            tempMessage.setDate(m.getDate());
            tempMessage.setTime(m.getTime());
            tempMessage.setFontColor(m.getFontColor());
            tempMessage.setFontFamily(m.getFontFamily());
            tempMessage.setFontSize(m.getFontSize());
            tempMessage.setFontStyle(m.getFontStyle());
            
            //add to collection 
            messageCollection.add(tempMessage);
         }
            
            //start saving 
             //JAXBElement<Chat> creatingChat  =factory.createChat(newChat);
             Marshaller marsh = context.createMarshaller();
             marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
             marsh.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,file.getName()+".xsd");
             marsh.setProperty("com.sun.xml.internal.bind.xmlHeaders",
                    "<?xml-stylesheet type='text/xsl' href='"+file.getName()+".xsl'?>");
           
             FileOutputStream fileOutputStream = new FileOutputStream(file+".xml"); 
              marsh.marshal(newChat,fileOutputStream );
              
              System.out.println(file);
              System.out.println(file.getParent());
              System.out.println(file.getName());
              System.out.println(file.getCanonicalPath());
                 
              
              saveFileInternal(getClass().getResource("style.xsl").openStream(),file.getParent() +"/"+file.getName()+".xsl");
              System.out.println(file.getName()+".xsl");
              
              saveFileInternal(getClass().getResource("chat.xsd").openStream(),file.getParent() +"/"+file.getName()+".xsd");
              System.out.println(file.getName()+".xsd");            
    }
    
     public static void saveFileInternal(InputStream is, String path) 
     {
        Thread threadOne = new Thread( () -> {
        
            FileOutputStream os = null;
            try {
                File newFile = new File(path);
                os = new FileOutputStream(newFile);
                int readByte ; 
                while((readByte=is.read())!= -1){
                    os.write(readByte);
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                  ex.printStackTrace();
                }
            }
        
        });
         
        threadOne.start();
          
      
     }
    
}
