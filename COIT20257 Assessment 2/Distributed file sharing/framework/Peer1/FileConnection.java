import java.net.*;
import java.io.*;
/**
 * FileConnection.java
 * @author      :Gwen Cabangon
 * @studentid   :12119839
 * @lecturer    :Wei Li
 * @Tutor       :Dat Huynh
 * 
 */
public class FileConnection extends Thread {
    private Socket fileSocket;
    private InputStream in;
    private ProjectPeerController controller;

    public FileConnection() {

    }

    public FileConnection (Socket fileSocket) {
        try{
            this.fileSocket = fileSocket;
            this.in = fileSocket.getInputStream();
        } catch (IOException e){
            System.out.println("Error when getting streams: "+e.getMessage());
        }
    }

    //set controller object
    public void setController(ProjectPeerController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {

        while (true) {
            try {
                //Construct data input stream to receive class files
                DataInputStream receivedResponse = new DataInputStream(in);
                String receivedMsg = receivedResponse.readUTF();
                String[] msg = receivedMsg.split("-");
                String peerWithFile = msg[0];
                String downloadFileName = msg[1];
                
                //check if file has already been downloaded
                if((new File("SharingFiles\\Downloaded"+downloadFileName)).exists()){
                    controller.setTextArea("File " + downloadFileName + " cannot be copied. It has already been downloaded.");
                } else {
                    controller.setTextArea("File found in " + peerWithFile);
                    System.out.println("File found in " + peerWithFile + " " +downloadFileName);
                    //get file length
                    long fileLength = receivedResponse.readLong();
                    File downloadedFile = new File(downloadFileName);
                    System.out.println(downloadedFile);
                    //InputStream fis = new FileInputStream(downloadedFile);
                    BufferedInputStream bis = new BufferedInputStream(receivedResponse);
                    BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(downloadedFile));
                    byte[] buffer = new byte[(int) fileLength];
                    int bytesRead;
                    while((bytesRead = bis.read(buffer)) > -1){
                        fos.write(buffer, 0, bytesRead);
                    }
                    //fis.close();
                    fos.close();
                    transferToFolder(downloadFileName, "SharingFiles");
                }//end else

            } catch (EOFException e) {
                System.out.println("EOF"+e.getMessage());
                break;
            } catch (SocketException e) {
                    //fileSocket.close();
                    System.out.println("Client closed.");
                    break; 
            } catch (IOException e) {
                e.printStackTrace(); 
                break;
            } finally {
                if(fileSocket != null){
                    try {
                        fileSocket.close();
                    } catch (IOException e){
                        System.out.println("close:"+e.getMessage());
                    }
                }
                break;
            }

        }

    }//end run

     //method to copy file into the new directory
     public void transferToFolder(String fileName, String path) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        File file = new File(fileName);
        try {
            is = new FileInputStream(file);
            os = new FileOutputStream(path+"\\Downloaded"+file);

            // buffer
            byte[] buf = new byte[(int) file.length()]; 

            int bytesRead;
            while ((bytesRead = is.read(buf)) > -1) {
                os.write(buf, 0, bytesRead);
            }

            os.close();
            controller.setTextArea("Download Successful: " + fileName);

        } catch (IOException e) {
            e.printStackTrace(); 
        } finally {
            is.close();
            file.delete();
        }
    }
 
}