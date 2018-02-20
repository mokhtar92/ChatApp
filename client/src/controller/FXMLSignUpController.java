package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import com.jfoenix.controls.JFXRadioButton;
import entity.User;
import factory.AvatarCallback;
import factory.AvatarListCell;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Service;

/**
 *
 * @author Ahmed_Mokhtar
 */
public class FXMLSignUpController implements Initializable {

    private Stage myStage;
    private double xOffset = 0;
    private double yOffset = 0;
    Service service = new Service();
    ObservableList<String> options;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField confirmPasswordTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private JFXRadioButton maleRadioBtn;

    @FXML
    private Label invalidImage;

    @FXML
    private Label invalidIFName;

    @FXML
    private Label invalidLname;

    @FXML
    private Label invalidEmail;
    
    @FXML
    private Label invalidCountry;

    @FXML
    private Label invalidPassword;

    @FXML
    private Label invalidConfirmPassword;
    
    @FXML
    private Label invalidDate;
    
    @FXML
    private ComboBox<String> ChooseAvatarComboBox;

    @FXML
    private DatePicker signUpDatePicker;

    public FXMLSignUpController() {
    }

    private boolean validation(User user) {
        boolean isValid = true;

        invalidImage.setVisible(false);
        invalidIFName.setVisible(false);
        invalidLname.setVisible(false);
        invalidEmail.setVisible(false);
        invalidCountry.setVisible(false);
        invalidPassword.setVisible(false);
        invalidConfirmPassword.setVisible(false);
        invalidDate.setVisible(false);

        if (!user.setFirstName(firstNameTextField.getText())) {
            isValid = false;
            invalidIFName.setVisible(!isValid);
        }
        if (!user.setLastName(lastNameTextField.getText())) {
            isValid = false;
            invalidLname.setVisible(!isValid);
        }
        if (!passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
            isValid = false;
            invalidPassword.setVisible(!isValid);
            invalidConfirmPassword.setVisible(!isValid);
        }
        if (!user.setPassword(passwordTextField.getText())) {
            isValid = false;
            invalidPassword.setVisible(!isValid);
            invalidConfirmPassword.setVisible(!isValid);
        }
        if (!user.setEmail(emailTextField.getText())) {
            isValid = false;
            invalidEmail.setVisible(!isValid);
        }

        LocalDate localDate = signUpDatePicker.getValue();
        if (localDate != null) {
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            if (!user.setBirthDate(date)) {
                isValid = false;
                invalidDate.setVisible(!isValid);
            }

        } else {
            isValid = false;
            invalidDate.setVisible(!isValid);
        }

        if (!user.setCountry(countryTextField.getText())) {
            isValid = false;
            invalidCountry.setVisible(!isValid);
        }

        if (maleRadioBtn.isSelected()) {
            user.setGender("male");
        }

        if (!maleRadioBtn.isSelected()) {
            user.setGender("female");
        }

        if (ChooseAvatarComboBox.getSelectionModel().getSelectedIndex() == -1 || (!user.setImgURL(options.get(ChooseAvatarComboBox.getSelectionModel().getSelectedIndex())))) {
            isValid = false;
            invalidImage.setVisible(!isValid);
        }
        return isValid;
    }

    @FXML
    private void signUp(ActionEvent ev) {
        User user = new User();
        if(Service.getServer()!=null){
        if (validation(user) && service.signUp(user)) {
            try {

                Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLLoginScreen.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        } else {
            //invalidGeneralLable.setVisible(true);
        }
    }
        else{
             Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLServerOfflineScreen.fxml"));
                        Scene scene = new Scene(root);
                        myStage.setScene(scene);

                    } catch (IOException ex) {
                        Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) firstNameTextField.getScene().getWindow();
            }
        });

        displayComboBox();
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
    
    public void goBackToLogin(){
        Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLLoginScreen.fxml"));
                        Scene scene = new Scene(root);
                        myStage.setScene(scene);

                    } catch (IOException ex) {
                        Logger.getLogger(FXMLChatScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
    }

    //private implementation of methods
    private void move(MouseEvent event) {
        myStage.setX(event.getScreenX() - xOffset);
        myStage.setY(event.getScreenY() - yOffset);
    }

    private void displayComboBox() {
        options = FXCollections.observableArrayList();
        options.addAll("/resources/grandpa.png", "/resources/grandma.png",
                "/resources/man.png", "/resources/woman.png",
                "/resources/boy.png", "/resources/girl.png");
        ChooseAvatarComboBox.setItems(options);
        ChooseAvatarComboBox.setButtonCell(new AvatarListCell());
        ChooseAvatarComboBox.setCellFactory(new AvatarCallback());
    }

}
