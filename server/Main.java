package server;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileServer server = new FileServer();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String[] input = scanner.nextLine().split("\\s+");
            String command = input[0];
            String name;
            switch (command) {
                case "add":
                    name = input[1];
                    if (server.add(name)) {
                        System.out.println("The file " + name + " added successfully");
                    } else {
                        System.out.println("Cannot add the file " + name);
                    }
                    break;
                case "get":
                    name = input[1];
                    if (server.get(name)) {
                        System.out.println("The file " + name + " was sent");
                    } else {
                        System.out.println("The file " + name + " not found");
                    }
                    break;
                case "delete":
                    name = input[1];
                    if (server.delete(name)) {
                        System.out.println("The file " + name + " was deleted");
                    } else {
                        System.out.println("The file " + name + " not found");
                    }
                    break;
                case "exit":
                    System.exit(0);
                    break;
            }
        }
    }
}