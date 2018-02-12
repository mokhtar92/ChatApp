/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facilites;

import entity.Message;
import entity.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 *
 * @author Ahmed
 */
public class FileChoosertest extends Application {

//    @Override
//    public void start(Stage primaryStage) {
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });
//        
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        
//        Scene scene = new Scene(root, 300, 250);
//        
//        primaryStage.setTitle("Hello World!");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        launch(args);
//    }
    // static File testFile ;
    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(final Stage primaryStage) {
        Group root = new Group();
        FontFamily f = FontFamily.ARIAL;

        Message test = new Message(10, "from", "String to", "String fontColor", "String fontFamily",
                f.toString(), "String body", "String fontWeight", true);
        Message test2 = new Message(10, "from", "String to", "String fontColor", "String fontFamily",
                f.toString(), "String body", "String fontWeight", true);
        List<Message> arr = new ArrayList<>();
        arr.add(test);

        User userTest = new User("email", "password");
        Button buttonLoad = new Button("Load");
        buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                try {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("xml files (*.xml)", "xml");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(primaryStage);
                    System.out.println(file);
                    //testFile=file;
                    createFile(file, (ArrayList<Message>) arr, userTest);

                } catch (IOException ex) {
                    Logger.getLogger(FileChoosertest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        VBox vBox = VBoxBuilder.create()
                .children(buttonLoad)
                .build();
        root.getChildren().add(vBox);
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }

    public static void createFile(File file, ArrayList<Message> messages, User user) throws FileNotFoundException, IOException {
        try {

            JAXBContext context = JAXBContext.newInstance("facilites");
            ObjectFactory factory = new ObjectFactory();
            HistoryType history = factory.createHistoryType();
            history.setOwner(user.getFirstName()+user.getLastName());
            List<MessageType> messagesCollection = history.getMessage();

            for (Message message : messages) {
                MessageType messageType = factory.createMessageType();
                messageType.setBody(message.getBody());
                messageType.setColor(message.getFontColor());
                // messageType.setDate(message.getDate());
//                messageType.setFamily(FontFamily.fromValue(message.getFontFamily()));
                messageType.setFrom(message.getFrom());
                messageType.setStyle(message.getFontStyle());
                messageType.setTo(message.getTo());
                messageType.setSize(message.getFontsSize());
                messageType.setWeight(message.getFontWeight());
                String decoration = (message.getUnderline()) ? "underline" : "none";
                messageType.setDecoration(decoration);

                messagesCollection.add(messageType);

            }

            JAXBElement<HistoryType> createHistory = factory.createHistory(history);
            Marshaller marsh = context.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //-------------- set Schcema ------------------
            marsh.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "historySchema.xsd");
            //-------------- set XLST ------------------
            marsh.setProperty("com.sun.xml.internal.bind.xmlHeaders",
                    "<?xml-stylesheet type='text/xsl' href='history.xsl'?>");

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            marsh.marshal(createHistory, fileOutputStream);
            fileOutputStream.close();

        } catch (JAXBException ex) {
            ex.printStackTrace();
            // Logger.getLogger(XmlHandle.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
