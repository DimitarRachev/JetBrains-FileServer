package server;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {
        FileServerInterface fileServerInterface = new FileServerInterface();
//        ExecutorService executor = Executors.newFixedThreadPool(4);
//        executor.submit(fileServerInterface::run);
        fileServerInterface.run();
    }
}