/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

/**
 * ProjectPeer.java
 * @author      :Gwen Cabangon
 * @studentid   :12119839
 * @lecturer    :Wei Li
 * @Tutor       :Dat Huynh
 * 
 */
public class ProjectPeer extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectPeer.fxml"));
        Parent root = loader.load();
        ProjectPeerController controller = (ProjectPeerController) loader.getController(); 
        Scene scene = new Scene(root, 580, 477);
        
        primaryStage.setTitle("Peer-to-Peer Overlay and File Sharing: Peer 1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
