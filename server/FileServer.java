package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    String address = "127.0.0.1";
    int port = 2222;
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream input;
    DataOutputStream output;


    public FileServer() {
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String start() {
        try {
            socket = serverSocket.accept();
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            return "Server started!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Problem with server start!";
        }
    }

   String read() {
       try {
           return "Received: " + input.readUTF();
       } catch (IOException e) {
           e.printStackTrace();
           return "Error while reading!";
       }
   }

   String send() {
       try {
           output.writeUTF("All files were sent!");
           return "Sent: All files were sent!";
       } catch (IOException e) {
           e.printStackTrace();
           return "Problem while sending!";
       }
   }

   void close() {
       try {
           serverSocket.close();
           socket.close();
           input.close();
           output.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}
