package server;


import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
       FileServerInterface fileServerInterface = new FileServerInterface();
       fileServerInterface.run();
    }
}