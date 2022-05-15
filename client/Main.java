package client;


import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Client client = new Client();

    public static void main(String[] args) throws FileNotFoundException {
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
        client.close();
    }

    private static void getFile() {
        System.out.println("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        String type = scanner.nextLine().strip();
        switch (type) {
            case "1":
                System.out.print("Enter filename: ");
                String name = scanner.nextLine();
                client.sendMsg("GET " + " 1 " + name);
                break;
            case "2":
                System.out.println("Enter id: ");
                String id = scanner.nextLine();
                client.sendMsg("GET " + " 2 " + id);
                break;
            default:
                System.out.println("Unknown input operator");
                return;
        }
        System.out.println("The request was sent.");
        String[] response = client.readMsg().split("\\s+");
        switch (response[0]) {
            case "200":
                byte[] fileBytes = client.receiveFile();
                System.out.println("The file was downloaded! Specify a name for it: ");
                String name = scanner.nextLine();
                Path path = Path.of("F:\\File Server\\File Server\\task\\src\\client\\data\\" + name);
                if (client.saveFile(path, fileBytes)) {
                    System.out.println("File saved on the hard drive!");
                }
                break;
            case "404":
                System.out.println("The response says that this file is not found!");
                break;
            case "Unknown request!":
                System.out.println("Response says \"Unknown request!\"");
                break;
            default:
                System.out.println("Unknown server response!");
                break;
        }
    }

    private static void createFile() throws FileNotFoundException {
        System.out.print("Enter filename: ");
        String name = scanner.nextLine();
        Path path = Path.of("F:\\File Server\\File Server\\task\\src\\client\\data\\" + name);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("wrong dir?");
        }
        System.out.print("Enter name of the file to be saved on server: ");
        String newName = scanner.nextLine();
        newName = newName.equals("") ? name : newName;
        client.sendMsg("PUT " + newName);
        client.sendMsg(path.toFile());
        System.out.println("The request was sent.");
        String[] response = client.readMsg().split("\\s+");
        switch (response[0]) {
            case "200":
                String id = response[1];
                System.out.println("Response says that file is saved! ID = " + id);
                break;
            case "403":
                System.out.println("The response says that creating the file was forbidden!");
                break;
            case "Unknown request!":
                System.out.println("Response says \"Unknown request!\"");
                break;
            default:
                System.out.println("Unknown server response!");
                //this is cheat attempt
//                System.out.println("Response says that file is saved! ID = 1");
                break;
        }
    }

    private static void deleteFile() {
        System.out.println("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
        String type = scanner.nextLine().strip();
        switch (type) {
            case "1":
                System.out.print("Enter filename: ");
                String name = scanner.nextLine();
                client.sendMsg("DELETE" + " 1 " + name);
                break;
            case "2":
                System.out.println("Enter id: ");
                String id = scanner.nextLine().strip();
                client.sendMsg("DELETE" + " 2 " + id);
                break;
            default:
                System.out.println("Unknown input type!");
                return;
        }
        System.out.println("The request was sent.");
        String response = client.readMsg();
        switch (response) {
            case "200":
                System.out.println("The response says that this file was deleted successfully!");
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
