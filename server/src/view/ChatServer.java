package view;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ServerImpl;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class ChatServer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        if(!ServerImpl.isServerFlag()){
        Parent root = FXMLLoader.load(getClass().getResource("FXMLServerScreen.fxml"));
        Scene scene = new Scene(root);
      
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    //System.exit(0);
                    if (ServerImpl.isServerFlag()) {
                        ServerImpl.stopServer();
                    }
                    System.exit(0);
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        stage.show();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
