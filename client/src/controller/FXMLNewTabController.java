/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Message;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLNewTabController implements Initializable{

    @FXML
    private TextField sendTextField;
    
    @FXML
    private ColorPicker messageColorPicker;

    @FXML
    private ComboBox<String> fontSizeComboBox;

    @FXML
    private ComboBox<String> fontStyleComboBox;

    @FXML
    private ComboBox<String> fontFamilyComboBox;
  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        setMessageFormatter();
        
    }
    
    public void saveChatHistory() {

    }

    public void sendFile() {

    }
    
    
    //private methods implmentation
    
    private void setMessageFormatter() {
        fontFamilyComboBox.getItems().addAll("Arial", "Segoe UI Semibold", "Bell MT", "Brush Script MT");
        fontFamilyComboBox.setValue("Arial");
        fontFamilyComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-font-family: \"" + newValue + "\";");
            }
        });

        fontSizeComboBox.getItems().addAll("10", "12", "14", "16");
        fontSizeComboBox.setValue("10");
        fontSizeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-font-size: " + newValue + "pt;");
            }
        });

        fontStyleComboBox.getItems().addAll("normal", "bold", "italic");
        fontStyleComboBox.setValue("regular");
        fontStyleComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String previousStyle = sendTextField.getStyle();
                if (newValue.equals("bold")) {
                    sendTextField.setStyle(previousStyle + "-fx-font-weight: " + newValue + ";");
                } else if (newValue.equals("italic")) {
                    sendTextField.setStyle(previousStyle + "-fx-font-style: " + newValue + ";");
                } else {
                    sendTextField.setStyle("-fx-font-style: " + newValue + ";");
                }
            }
        });

        messageColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String hexColor = Integer.toHexString(messageColorPicker.getValue().hashCode());
                String previousStyle = sendTextField.getStyle();
                sendTextField.setStyle(previousStyle + "-fx-text-inner-color: #" + hexColor + ";");
            }
        });
    }
    
}
