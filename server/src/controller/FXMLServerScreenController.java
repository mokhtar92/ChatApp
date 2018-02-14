package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import entity.User;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.NotificationImpl;
import model.Operation;
import model.ServerImpl;
import org.controlsfx.control.GridView;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLServerScreenController implements Initializable{

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
     PieChart chart2;
    
    @FXML
     Button updateBtn;
    
    @FXML
      GridView myGrid;      
      
    
    Operation operation = null;
    ObservableList<User> data;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        operation = new Operation();

        drawStatistics();

        try {

            ArrayList<User> users = new ArrayList<>();
            users = operation.getUsers();

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
                new PropertyValueFactory<User, String>("status"));
        country.setCellValueFactory(
                new PropertyValueFactory<User, String>("country"));

        usersTable.setItems(data);

        stop.setDisable(true);
        start.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                NotificationImpl impl = new NotificationImpl();
                impl.createNotification("hgh", "jhgh", "resources/chat_logo.png");
                ServerImpl.startSer();
                stop.setDisable(false);
                start.setDisable(true);

            }
        });

        updateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                drawStatistics();
            }
        });

        stop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try {
                    ServerImpl.stop();
                    start.setDisable(false);
                    stop.setDisable(true);
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(FXMLServerScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    
    public void drawStatistics ()
    {

        try {

            IntegerProperty maleNumber = new SimpleIntegerProperty(operation.maleFemale("male"));
            IntegerProperty femaleNumber = new SimpleIntegerProperty(operation.maleFemale("female"));

            Data d11 = new Data("male", maleNumber.doubleValue());
            //d11.pieValueProperty().bind(maleNumber);
            d11.pieValueProperty().bindBidirectional(maleNumber);

            Data d12 = new Data("female", femaleNumber.doubleValue());
            d12.pieValueProperty().bindBidirectional(femaleNumber);

            ObservableList<Data> sourceData1 = FXCollections.observableArrayList(d11, d12);

            chart.setData(sourceData1);

            System.out.println(maleNumber.getValue());
            System.out.println(d11.pieValueProperty().isBound());
            System.out.println(chart.getData());

        } catch (SQLException ex) {
            Logger.getLogger(FXMLServerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

  

    
}

