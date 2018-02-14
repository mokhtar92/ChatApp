package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import entity.User;
import interfaces.ServerInt;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.NotificationImpl;
import model.Operation;
import model.ServerDbOperation;
import model.ServerImpl;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLServerScreenController implements Initializable {

    @FXML
    Button stop;
    @FXML
    Button start;
    @FXML
    TableView<User> usersTable;
    @FXML
    TableColumn<User, String> firstName;
    @FXML
    TableColumn<User, String> lastName;
    @FXML
    TableColumn<User, String> email;
    @FXML
    TableColumn<User, String> gender;
    @FXML
    TableColumn<User, String> status;
    @FXML
    TableColumn<User, String> country;
    @FXML
    PieChart chart;
    @FXML 
    TextArea annoncementTextArea ;
  
    ObservableList<User> data;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                new PieChart.Data("male", 50),
                new PieChart.Data("female", 40)
        );
        chart.setData(list);
        
        try {

            ArrayList<User> users = new ArrayList<>();
            
            users=ServerDbOperation.getUser();
            this.data = FXCollections.observableArrayList();
            for (User user : users) {
                data.add(user);

            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLServerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        firstName.setCellValueFactory(
                new PropertyValueFactory<User, String>("firstName"));
        lastName.setCellValueFactory(
                new PropertyValueFactory<User, String>("lastName"));
        email.setCellValueFactory(
                new PropertyValueFactory<User, String>("email"));
        gender.setCellValueFactory(
                new PropertyValueFactory<User, String>("gender"));
        status.setCellValueFactory(
                new PropertyValueFactory<User, String>("myStatus"));
        country.setCellValueFactory(
                new PropertyValueFactory<User, String>("country"));

        usersTable.setItems(data);

        stop.setDisable(true);
        start.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ServerImpl.startServer();
                stop.setDisable(false);
                start.setDisable(true);

            }
        });

        stop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try {
                    ServerImpl.stopServer();
                    start.setDisable(false);
                    stop.setDisable(true);
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(FXMLServerScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    @FXML
    public void SendAnnoncement(MouseEvent event) throws RemoteException{
        String msg=annoncementTextArea.getText().trim();
        if(msg!=null&&msg!=""){
            
            ServerImpl.sendAnnoncement(msg);
        }
    }
}
