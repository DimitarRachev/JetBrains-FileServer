package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    String address = "127.0.0.1";
    int port = 2222;
    Socket socket;
    DataInputStream input;
    DataOutputStream output;

    void connect() {
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMsg(String msg) {
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String readMsg() {
        try {
            return input.readUTF();
        } catch (IOException e) {
           return e.toString();
        }
    }

    void close() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
