package sg.edu.nus.iss.baccarat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    public static void main(String[] args) {
        //spawning threads for socket
        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("Utilising " + coreCount + " threads for Baccarat server");
        ExecutorService threadService = Executors.newFixedThreadPool(coreCount);

        //setting up baccarat game
        BaccaratEngine baccaratEngine = new BaccaratEngine(Integer.parseInt(args[1]));
        System.out.printf("Baccarat game using %s decks of cards\n", args[1]);

        //setting up server socket
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            System.out.printf("Server started on port %s\n", args[0]);

            while (true) {
                Socket clientConnectionSocket = serverSocket.accept();
                threadService.submit(new ServerConnectionHandler(clientConnectionSocket));
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}