package client;


public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        System.out.println("Client started!");
        client.connect();
        String msg = "Give me everything you have!";
        client.sendMsg(msg);
        System.out.println("Sent: " + msg);
        System.out.println("Received: " + client.readMsg());
        client.close();
    }
}
