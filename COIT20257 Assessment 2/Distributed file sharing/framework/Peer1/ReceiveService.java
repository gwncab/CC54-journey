import java.net.*;
import java.io.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.*;
/**
 * ReceiveService.java
 * @author      :Gwen Cabangon
 * @studentid   :12119839
 * @lecturer    :Wei Li
 * @Tutor       :Dat Huynh
 * 
 */
public class ReceiveService extends Thread {
    private MulticastSocket multicastSocket;
    private String peerID;
    private InetAddress groupIP;
    private int groupPort = 0;
    private String mes = "";
    private String received = "";
    private ProjectPeerController controller;
    private final int soTime = 5000;
    
    //no parameter constructor
    public ReceiveService () {

    }

    //parametized constructor
    public ReceiveService (String peerID, InetAddress groupIP, int groupPort)  {
        this.peerID = peerID;
        this.groupIP = groupIP;
        this.groupPort = groupPort;
    }

    //set controller object
    public void setController(ProjectPeerController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        String[] arr;
        String requestingPeerID = "";
        String fname = "";
        String filePort = "";
        Boolean found = false;
        try {
            multicastSocket = new MulticastSocket(groupPort);
            multicastSocket.joinGroup(groupIP);
            
            //loop
            while (true) {
                byte[] buffer = new byte[1000*16];
                DatagramPacket receivedMessage = new DatagramPacket(buffer, buffer.length);
                System.out.println("Waiting...");

                multicastSocket.receive(receivedMessage);
                String msg = new String (receivedMessage.getData()).trim();
                arr = msg.split("-");
                requestingPeerID = arr[0];
                fname = arr[1];
                filePort = arr[2];
                InetAddress senderIP = receivedMessage.getAddress(); //get ip of the sender

                //multicastSocket.setSoTimeout(soTime);
                if(!peerID.equals(requestingPeerID)) {
                    //find file
                    File requestFile = new File("SharingFiles\\"+fname);
                    if(requestFile.exists()) {
                        controller.setTextArea("---\nFile found: " + requestFile + " in " + peerID );
                        //invoke sendResponse to respond back to requesting peer
                        sendResponse(peerID, senderIP, Integer.parseInt(filePort), requestFile, fname);

                        //multicastSocket.setSoTimeout(0);
                    } //end file exists
                }
                
                
            }//end while true

        } catch (SocketException e){
            System.out.println("ReceiveService Socket: " + e.getMessage());
            mes = "ReceiveService Socket: " + e.getMessage();
            controller.setTextArea(mes);
        } catch (IOException e){
            /*try {
                if (peerID.equals(requestingPeerID) && multicastSocket.getSoTimeout() == soTime) {
                    //controller.setTextArea(fname + " cannot be found in the overlay.");
                    controller.fileNotFound(fname + " cannot be found in the overlay.", found);
                    //controller.restart();
                } else controller.restart();
            } catch (SocketException se) {
                System.out.println("ReceiveService IO Socket: " + se.getMessage());
            }*/
            System.out.println("**************");
            System.out.println("ReceiveService IO: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Array: " + e.getMessage());
            e.printStackTrace();
            //mes = "Receive IO: " + e.getMessage();
            //controller.setTextArea(mes);
        }
        
    }//end of run

    //sending response via tcp
    public void sendResponse(String peerID, InetAddress senderIP, int filePort, File requestFile, String fileName) {
        Socket tcpSocket = null;
        OutputStream out = null;
        controller.setTextArea("Sending response...\n---");
        System.out.println("Sending response...\n---");
        try {
            
            tcpSocket = new Socket(senderIP, filePort);
            out = tcpSocket.getOutputStream();
            //send confirmation file found in peer and file name
            DataOutputStream dos = new DataOutputStream(out);

            dos.writeUTF(peerID + "-" + fileName);
            //read file into a byte array
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream  (requestFile));
            BufferedOutputStream bos = new BufferedOutputStream(dos);
            //get length
            long length = requestFile.length();
            byte[] buffer = new byte[(int) length];
            //send file length
            dos.writeLong(length);
            int count;
            while ((count = bis.read(buffer)) > -1){
                dos.write(buffer,0,count);
            }

            out.flush();
            
        } catch (UnknownHostException uhe) {
            System.out.println("UnknowHost:"+uhe.getMessage());
        } catch (IOException e) {
            System.out.println(peerID + " IO errors:"+e.getMessage());
            //e.printStackTrace();
        } finally {
            if(tcpSocket != null){
                try {
                    tcpSocket.close();
                } catch (IOException e){
                    System.out.println("close:"+e.getMessage());
                }
            }
        }
    }
}