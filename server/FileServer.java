package server;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileServer {
    String address = "127.0.0.1";
    int port = 2222;
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream input;
    DataOutputStream output;
    volatile Map<Long, Path> fileMap;
    private Path fileMapPath;
    private long lastId;


    public FileServer() {
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
            lastId = 0;
            fileMap = getFileMap();
            fileMapPath = Path.of(System.getProperty("user.dir"), "File Server", "task", "src", "server", "data", "fileMap");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<Long, Path> getFileMap() {
        if (fileMapExist()) {
            return readFileMapAndSetSize();
        }
        lastId = 0;
        return new HashMap<>();
    }

    private Map<Long, Path> readFileMapAndSetSize() {
        try {
            Scanner scanner = new Scanner(fileMapPath);
            Map<Long, Path> fileMap = new HashMap<>();
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("#");
                long ID = Long.parseLong(line[0]);
                lastId = Math.max(lastId, ID);
                Path path = Path.of(line[1]);
                fileMap.put(ID, path);
            }
            scanner.close();
            return fileMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private boolean fileMapExist() {
        fileMapPath = Path.of(System.getProperty("user.dir"), "File Server", "task", "src", "server", "data", "fileMap");
        return fileMapPath.toFile().isFile();
    }

    String start() {
        try {
            socket = serverSocket.accept();
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            return "Server started!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Problem with server start!";
        }
    }

    String readHello() {
        try {
            return input.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error while reading!";
        }
    }

    String sendMsg(String msg) {
        try {
            output.writeUTF(msg);
            return "Sent: " + msg;
        } catch (IOException e) {
            e.printStackTrace();
            return "Problem while sending!";
        }
    }

    void close() {
        try {
            saveFileMap();
            serverSocket.close();
            socket.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFileMap() {
        if (fileMapPath.toFile().isFile()) {
            fileMapPath.toFile().delete();
        }
        try {
            fileMapPath.toFile().createNewFile();
            Writer writer = new OutputStreamWriter(new FileOutputStream(fileMapPath.toFile(), false));
            for (var entry : fileMap.entrySet()) {
                writer.write(entry.getKey() + "#" + entry.getValue() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public byte[] readFile() {
        try {
            int size = input.readInt();
            byte[] fileBytes = new byte[size];
            input.read(fileBytes);
            return fileBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long makeId(Path root) {
        fileMap.put(++lastId, root);
        return lastId;
    }

    public boolean sendFile(File file) {
        try {
            InputStream fileStream = new BufferedInputStream(new FileInputStream(file));
            byte[] fileBytes = fileStream.readAllBytes();
            output.writeInt(fileBytes.length);
            output.write(fileBytes);
//            int size = 0;
//            output.writeInt(size);
            output.flush();
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Path getById(Long id) {
        return fileMap.get(id);
    }
}
