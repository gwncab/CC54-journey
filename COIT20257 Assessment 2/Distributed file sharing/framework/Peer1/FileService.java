import java.net.*;
import java.io.*;

/**
 * FileService.java
 * @author      :Gwen Cabangon
 * @studentid   :12119839
 * @lecturer    :Wei Li
 * @Tutor       :Dat Huynh
 * 
 */
public class FileService extends Thread{
    
    private int serverPort;
    private ServerSocket serverSocket = null;
    private ProjectPeerController controller;
    FileConnection connection = new FileConnection();
    
    public FileService(int serverPort, ProjectPeerController controller){
        this.serverPort = serverPort;
        this.controller = controller;
    }
    
    public void run(){
        try{
            
            serverSocket = new ServerSocket(serverPort);
            //System.out.println("------------------------------");
            System.out.println("File service is listening on port "+ serverPort +" for file transfer...");
            System.out.println("------------------------------");
           
            while(true){
                //stop the program until there is a connection from the client
                Socket socket = serverSocket.accept();
                //accept returns a socket to communicate with the client
                connection = new FileConnection(socket);
                connection.start();
                connection.setController(controller);
            }
            
        } catch (IOException e) {
            System.out.println("Error when listening to a connection: " + e.getMessage());
            controller.setTextArea("File port in use. Please provide a different port.");
        }

    }

}