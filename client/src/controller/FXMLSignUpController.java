package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import com.jfoenix.controls.JFXRadioButton;
import factory.AvatarCallback;
import factory.AvatarListCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


/**
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLSignUpController implements Initializable{

    private Stage myStage;
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField userameTextField;

    @FXML
    private JFXRadioButton maleRadioBtn;

    @FXML
    private ComboBox<String> ChooseAvatarComboBox;

    @FXML
    private ImageView avatarImageView;
    
    @FXML
    private DatePicker signUpDatePicker;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) avatarImageView.getScene().getWindow();
            }
        });
        
        displayComboBox();
    }
    
    public void createNewAccount() {

    }

    public void moveSignUpWindow(MouseEvent event) {
        move(event);
    }
   
    public void closeSignUpWindow() {
        myStage.close();
    }

    public void minimizeSignUpWindow(MouseEvent event) {
        myStage.setIconified(true);
    }
    
    public void setMousePosition(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    
    //private implementation of methods
    private void move(MouseEvent event) {
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }
    
    private void displayComboBox(){
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll("/resources/grandpa.png", "/resources/grandma.png",
                "/resources/man.png", "/resources/woman.png",
                "/resources/boy.png", "/resources/girl.png");
        ChooseAvatarComboBox.setItems(options);
        ChooseAvatarComboBox.setButtonCell(new AvatarListCell());
        ChooseAvatarComboBox.setCellFactory(new AvatarCallback());
    }
    
}
