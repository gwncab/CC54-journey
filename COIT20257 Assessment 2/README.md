# COIT20257
## Distributed Systems: Principles and Development

## Overview

The program Peer-to-Peer Overlay and File Sharing demonstrates file sharing using a framework that implements UDP datagram and TCP streaming to allow files to be requested by one peer, searched through the other peers in the overlay, and sent back or downloaded when found.

## Framework Components
- This program was developed using the following:
  - Java & Javac 1.8.0_261
  - Java Fx 11.0.2
  - SceneBuilder 11 to layout and link the GUI elements and define ActionEvents
- Peers
  - The framework consists of 3 peers: Peer 1, Peer 2, Peer 3
- Peer File Sharing Folders
  -  Each peer holds a SharingFiles folder which contains the file/s shared within the overlay
  -  Each peer hold different file types to demonstrate file sharing capability
  -  File types: image, ppt, pdf 
- Peer Class, GUI, and Controller
  - Each peer is composed of the following Java Class, FXML, and Controller files:
    - ProjectPeer.java contains the main() method to load the GUI and create the controller object.
    - ProjectPeer.fxml contains and defines JavaFx GUI elements.
    - ProjectPeerController.java holds the ActionEvents and declares the objects linked to the GUI elements.
    - ReceiveService is a multithreaded class containing the method which holds the multicast receiving end of the peer. The thread is triggered after clicking the ‘Set’ button in the GUI.
    - FileConnection.java is a threaded class containing the TCP receiving end to read and write (download) streamed files.
    - FileService.java is the entry point for the FileConnection class, where the ServerSocket for the TCP receive is set.
