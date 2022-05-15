package server;

public class Main {

    public static void main(String[] args) {
        FileServerInterface fileServerInterface = new FileServerInterface();
        fileServerInterface.run();
    }
}