package server;


public class Main {

    public static void main(String[] args) {
        FileServer server = new FileServer();
        System.out.println("Server started!");
        server.start();
        System.out.println(server.read());
        System.out.println(server.send());
        server.close();
    }
}