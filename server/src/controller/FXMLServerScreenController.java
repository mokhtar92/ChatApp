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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Operation;
import model.ServerDbOperation;
import model.ServerImpl;
import model.UserTable;

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
    public TableView<User> usersTable;
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
     PieChart chart2;
    
    @FXML
     Button updateBtn;
    @FXML 
    TextArea annoncementTextArea ;
  Operation operation = null;
   public ObservableList<User> data;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        operation = new Operation();
        drawStatistics();
        try {
            Draw2();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLServerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
       ServerImpl.setController(this);
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
        updateTabelView();
        stop.setDisable(true);
        start.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ServerImpl.startServer();
                stop.setDisable(false);
                start.setDisable(true);

            }
        });
        
        updateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              drawStatistics();
                try {
                    Draw2();
                } catch (RemoteException ex) {
                    Logger.getLogger(FXMLServerScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateTabelView();
              
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
            
            ServerImpl.sendAnnouncement(msg);
        }
    }
    public void drawStatistics ()
    {

        IntegerProperty maleNumber = new SimpleIntegerProperty(operation.maleFemale("male"));
        IntegerProperty femaleNumber = new SimpleIntegerProperty(operation.maleFemale("female"));
        Data d11 = new Data("male :"+maleNumber.intValue()+"", maleNumber.doubleValue());
        //d11.pieValueProperty().bind(maleNumber);
        d11.pieValueProperty().bindBidirectional(maleNumber);
        Data d12 = new Data("female :"+femaleNumber.intValue()+"", femaleNumber.doubleValue());
        d12.pieValueProperty().bindBidirectional(femaleNumber);
        ObservableList<Data> sourceData1 = FXCollections.observableArrayList(d11, d12);
        chart.setData(sourceData1);
        
    }
    public void updateTabelView(){
        UserTable table=new UserTable(usersTable);
        usersTable.setItems(table.getData());
    }
    
   public void Draw2() throws RemoteException
   {
    ServerImpl impl = new ServerImpl();
     int allOnlineUsers= impl.GetClientcount();
     int offLineUsers = operation.getAllUser()-allOnlineUsers;
     Data d11 = new Data("online :"+allOnlineUsers+"",allOnlineUsers);
     Data d22 = new Data("offline : "+offLineUsers+"", offLineUsers);
     ObservableList<Data> sourceData =FXCollections.observableArrayList(d11,d22);
     chart2.setData(sourceData);
     
     
   }
    
    
}
