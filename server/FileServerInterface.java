package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class FileServerInterface {
    FileServer server;
    Scanner scanner;

    public FileServerInterface() {
        server = new FileServer();
        scanner = new Scanner(System.in);
    }

    void run() throws IOException {
        System.out.println("Server started!");


        while (true) {
            server.start();
            String[] input = server.read().split("\\s+");
            Request request = Request.valueOf(input[0].toUpperCase());
            String name;
//            File file;
//            String path = " ./src/server/data";
            Path root;
//            Path path1;
            switch (request) {
                case GET:
                    name = input[1];
//                    root = Path.of(System.getProperty("user.dir"), "File Server", "task", "src", "server", "data", name);
                    root = Path.of(System.getProperty("user.dir"), "src", "server", "data", name);

//                    file = new File(root + "\\" + name);
//                    path1 = Path.of(path + "/" + name);
                    if (Files.exists(root)) {
                        List<String> fileData = Files.readAllLines(root);
                        String DATA = getDATA(fileData);
                        server.send("200 " + DATA);
                    } else {
                        server.send("404");
                    }
                    break;
                case PUT:
                    name = input[1];
//                    root = Path.of(System.getProperty("user.dir"), "File Server", "task", "src", "server", "data", name);
                    root = Path.of(System.getProperty("user.dir"), "src", "server", "data", name);

//                    path1 = Path.of(path + "/" + name);

//                    file = root.toFile();
                    String DATA = input[2];
                    if (Files.exists(root)) {
                        server.send("403");
                    } else {
//                        file.mkdirs();
                        Files.createDirectories(root.getParent());
//                        Files.createFile(root);
                        FileWriter writer = new FileWriter(root.toFile());
                        writer.append(DATA);
//                        writer.write(DATA);
//                        writer.flush();
                        writer.close();
                        server.send("200");
                    }
                    break;
                case DELETE:
                    name = input[1];
                    root = Path.of(System.getProperty("user.dir"), "src", "server", "data", name);
//                    root = Path.of(System.getProperty("user.dir"), "File Server", "task", "src", "server", "data", name);
//                    path1 = Path.of(path + "/" + name);

                    if (Files.deleteIfExists(root)) {
                        server.send("200");
                    } else {
                        server.send("404");
                    }
                    break;
                case EXIT:
                    server.close();
                    System.exit(0);
                default:
                    System.out.println("Unknown request!");
                    server.send("Unknown request!");
            }
        }
    }

    private String getDATA(List<String> fileData) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fileData.size(); i++) {
            sb.append(fileData.get(i));
            if (i != fileData.size() - 1) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
