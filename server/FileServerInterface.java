package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileServerInterface implements Runnable {
    FileServer server;
    Scanner scanner;
    ExecutorService executor;


    public FileServerInterface() {
        server = new FileServer();
        scanner = new Scanner(System.in);
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void run() {
        System.out.println("Server started!");


        while (true) {
            server.start();
            executor.submit(this::actionLogic);
//            actionLogic();
        }
    }

    private void actionLogic() {
        String[] input = server.readHello().split("\\s+");
        Request request = Request.valueOf(input[0].toUpperCase());
        String name;
        Path root;
        String type;
        switch (request) {
            case GET:
                type = input[1];
                switch (type) {
                    case "1":
                        name = input[2];
                        root = Path.of("F:\\File Server\\File Server\\task\\src\\server\\data\\" + name);

                        if (Files.exists(root)) {
                            server.sendMsg("200 ");
                            server.sendFile(root.toFile());
                        } else {
                            server.sendMsg("404");
                        }
                        break;
                    case "2":
                        Long id = Long.parseLong(input[2]);
                        Path path = server.getById(id);
                        if (path == null) {
                            server.sendMsg("404");
                        } else {
                            server.sendMsg("200 ");
                            server.sendFile(path.toFile());
                        }
                        break;
                }
                break;
            case PUT:
                name = input[1];
                root = Path.of("F:\\File Server\\File Server\\task\\src\\server\\data\\" + name);
                if (Files.exists(root)) {
                    server.sendMsg("403");
                } else {
                    byte[] fileBytes = server.readFile();
                    try {
                        root.toFile().createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(root.toFile());
                        fileOutputStream.write(fileBytes);
                        fileOutputStream.close();
                        Long id = server.makeId(root);
                        server.sendMsg("200 " + id);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case DELETE:
                type = input[1];
                switch (type) {
                    case "1":
                        name = input[2];
                        root = Path.of("F:\\File Server\\File Server\\task\\src\\server\\data\\" + name);

                        break;
                    case "2":
                        Long id = Long.parseLong(input[2]);
                        root = server.getById(id);
                        break;
                    default:
                        //todo check for better way to fix "may not be initialized" problem
                        root = Path.of("./nonExistingFile.txt");
                        break;
                }
                try {
                    if (Files.deleteIfExists(root)) {
                        server.sendMsg("200");
                    } else {
                        server.sendMsg("404");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case EXIT:
                executor.shutdown();
                server.close();
                System.exit(0);
            default:
                System.out.println("Unknown request!");
                server.sendMsg("Unknown request!");
        }
    }
}
