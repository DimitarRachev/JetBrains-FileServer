package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;

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

    void sendMsg(File file) {
        try {
            InputStream fileStream = new BufferedInputStream(new FileInputStream(file));
            byte[] fileBytes = fileStream.readAllBytes();
            output.writeInt(fileBytes.length);
            output.write(fileBytes);
            //TODo why I put this here? leftover? also is there a  fix for duplicating code in client and server?
            output.flush();
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

    public byte[] receiveFile() {
        int size = 0;
        try {
            size = input.readInt();
            byte[] fileBytes = new byte[size];
            input.read(fileBytes);
            return fileBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveFile(Path path, byte[] fileBytes) {
        try {
            path.toFile().createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
            fileOutputStream.write(fileBytes);
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
