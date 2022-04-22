/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.*;
import javafx.event.ActionEvent;
import java.net.*;
import java.io.*;
import java.nio.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import java.util.*;
/**
 * ProjectPeerController.java
 * @author      :Gwen Cabangon
 * @studentid   :12119839
 * @lecturer    :Wei Li
 * @Tutor       :Dat Huynh
 * 
 */
public class ProjectPeerController implements Initializable {

    /**
     * Initializes the controller class.
    */
    //declaration of fxml objects
    @FXML private TextField groupIPInput;
    @FXML private Label groupIPInputErr;
    @FXML private TextField groupPortInput;
    @FXML private Label groupPortInputErr;
    @FXML private TextField peerIDInput;
    @FXML private Label peerIDInputErr;
    @FXML private TextField receiptPortInput;
    @FXML private Label receiptPortInputErr;
    @FXML private Button setBtn;
    @FXML private TextField fileNameInput;
    @FXML private Button searchBtn;
    @FXML private TextArea resultArea;
    InetAddress groupIP = null;
    int groupPort = 0;
    String peerID;
    int receiptPort;
    ReceiveService receiveService;
    FileConnection fileConnection;
    MulticastSocket multicastSocket;
    FileService fileService;
    String regex = "^2(?:2[4-9]|3\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3}$";//regex for multicast ip

    //action event for Set button
    @FXML
    void setBtnClicked(ActionEvent event) {
        

        //clear gui error labels
        groupIPInputErr.setText("");
        groupPortInputErr.setText("");
        peerIDInputErr.setText("");
        receiptPortInputErr.setText("");

        //validate ip range
        try {
            if(groupIPInput.getText().matches(regex)) {
                groupIP = InetAddress.getByName(groupIPInput.getText());
                groupIPInputErr.setText("");
            } else throw new UnknownHostException("*");
        } catch (UnknownHostException uhe) {
            groupIPInputErr.setText(uhe.getMessage());
        }
            
        //validate groupPort input
        try {
            if (!groupPortInput.getText().isEmpty()) {
                groupPort = Integer.parseInt(groupPortInput.getText());
                groupPortInputErr.setText("");
            } else groupPortInputErr.setText("*");
        } catch (InputMismatchException e) {
            groupPortInputErr.setText("*");
        } catch (NumberFormatException e) {
            groupPortInputErr.setText("*");
        }

        //validate peerID not empty
        if(!peerIDInput.getText().isEmpty()) {
            peerID = peerIDInput.getText();
            peerIDInputErr.setText("");
        } else peerIDInputErr.setText("*");

        //validate receiptPort input
        try {
            if (!receiptPortInput.getText().isEmpty()) {
                receiptPort = Integer.parseInt(receiptPortInput.getText());
                receiptPortInputErr.setText("");
            } else receiptPortInputErr.setText("*");
        } catch (InputMismatchException e) {
            receiptPortInputErr.setText("*");
        } catch (NumberFormatException e) {
            receiptPortInputErr.setText("*");
        }

        try {

            if (groupIPInputErr.getText().isEmpty() && groupPortInputErr.getText().isEmpty() && peerIDInputErr.getText().isEmpty() && receiptPortInputErr.getText().isEmpty()) {
                if(multicastSocket != null) multicastSocket.close();

                multicastSocket = new MulticastSocket();
                multicastSocket.joinGroup(groupIP);

                //initialise receive service object
                receiveService = new ReceiveService(peerID, groupIP, groupPort);
                receiveService.setController(this);
                receiveService.start(); //invoke thread run()

                //start file server
                fileService = new FileService(receiptPort, this);
                fileService.start();

                setTextArea("Multicast socket and file server ready...");
                fileNameInput.setDisable(false);
                searchBtn.setDisable(false);

            }

        } catch (SocketException e){
            setTextArea("Socket: " + e.getMessage());
        } catch (IOException e){
            setTextArea("IO: " + e.getMessage());
        }

    } //end setBtn actionevent

    @FXML
    void searchBtnClicked(ActionEvent event) {
        String fname = fileNameInput.getText();
        File file = new File("SharingFiles\\"+fname);
        if(file.exists()) {
            setTextArea("************\n Requested file '"+fname+"' exists locally \n************");
        } else {
            try {
                String fileName = peerID + "-" + fname + "-" + receiptPort;
                byte [] m = fileName.getBytes();
                DatagramPacket messageOut = new DatagramPacket(m, m.length, groupIP, groupPort);
                //multicastSocket.setTimeToLive((int) 200);
                multicastSocket.send(messageOut);	//send to group by using the multicast object

            
            } catch (SocketException e){
                System.out.println("Broadcast Socket: " + e.getMessage());
            } catch (IOException e){
                System.out.println("IO: " + e.getMessage());
            } 
        }
        
    }//end searchbtn

    //set messages in text area
    void setTextArea(String text) {
        resultArea.appendText(text + "\n");
    }

    //restart receive services for each peer after sotimeout
    void restart() {
        receiveService = new ReceiveService(peerID, groupIP, groupPort);
        receiveService.setController(this);
        receiveService.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //set controller instance in receiveService
        receiveService = new ReceiveService();

        fileConnection = new FileConnection();

        //set default values
        groupIPInput.setText("228.5.6.7");
        groupPortInput.setText("8888");
        peerIDInput.setText("PPP2");
        receiptPortInput.setText("6789");

        fileNameInput.setDisable(true);
        searchBtn.setDisable(true);
    } //end initialize
    
}
