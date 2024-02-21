package sg.edu.nus.iss.baccarat.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    public static void main(String[] args) {
        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("Utilising " + coreCount + " threads for server");
        ExecutorService threadService = Executors.newFixedThreadPool(coreCount);

        BaccaratEngine baccaratEngine = new BaccaratEngine(Integer.parseInt(args[1]));
    }

}