package client;


import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Client client = new Client();

    public static void main(String[] args) {
        client.connect();

        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String n = scanner.nextLine().strip();
        switch (n) {
            case "1":
                getFile();
                break;
            case "2":
                createFile();
                break;
            case "3":
                deleteFile();
                break;
            case "exit":
                client.sendMsg("EXIT");
                System.out.println("The request was sent.");
                break;
        }
//        client.close();
    }

    private static void getFile() {
        System.out.print("Enter filename: ");
        String name = scanner.nextLine();
        client.sendMsg("GET " + name);
        System.out.println("The request was sent.");
        String[] response = client.readMsg().split("\\s+");
        switch (response[0]) {
            case "200":
                System.out.println("The content of the file is: " + response[1]);
                break;
            case "404":
                System.out.println("The response says that the file was not found!");
                break;
            case "Unknown request!":
                System.out.println("Response says \"Unknown request!\"");
                break;
            default:
                System.out.println("Unknown server response!");
                break;
        }
    }

    private static void createFile() {
        System.out.print("Enter filename: ");
        String name = scanner.nextLine();
        System.out.print("Enter file content: ");
        String content = scanner.nextLine();
        client.sendMsg("PUT " + name + " " + content);
        System.out.println("The request was sent.");
        String response = client.readMsg();
        switch (response) {
            case "200":
                System.out.println("The response says that the file was created!");
                break;
            case "403":
                System.out.println("The response says that creating the file was forbidden!");
                break;
            case "Unknown request!":
                System.out.println("Response says \"Unknown request!\"");
                break;
            default:
                System.out.println("Unknown server response!");
                break;
        }
    }

    private static void deleteFile() {
        System.out.print("Enter filename: ");
        String name = scanner.nextLine();
        client.sendMsg("DELETE " + name);
        System.out.println("The request was sent.");
        String response = client.readMsg();
        switch (response) {
            case "200":
                System.out.println("The response says that the file was successfully deleted!");
                break;
            case "404":
                System.out.println("The response says that the file was not found!");
                break;
            case "Unknown request!":
                System.out.println("Response says \"Unknown request!\"");
                break;
            default:
                System.out.println("Unknown server response!");
                break;
        }
    }
}
