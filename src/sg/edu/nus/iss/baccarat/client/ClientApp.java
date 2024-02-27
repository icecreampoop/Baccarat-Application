package sg.edu.nus.iss.baccarat.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) {
        ServerResponseHandler serverResponseHandler;
        ExecutorService thread = Executors.newSingleThreadExecutor();
        String[] stringSplit = args[0].split(":");
        String address = stringSplit[0];
        Integer port = Integer.parseInt(stringSplit[1]);
        Scanner scanner = new Scanner(System.in);
        String userInput;
        boolean userHasQuit = false;

        try (Socket socket = new Socket(address, port)) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            serverResponseHandler = new ServerResponseHandler(socket);
            thread.submit(serverResponseHandler);
            System.out.print("Please log in to your account: ");

            while (!userHasQuit) {
                userInput = scanner.nextLine().toLowerCase().trim();
                
                if (userInput.equals("")) {
                    System.out.println("Please enter a valid input!");
                    continue;
                }

                printWriter.println(userInput);

                if (userInput.equals("quit")) {
                    userHasQuit = true;

                    scanner.close();
                    printWriter.close();
                    thread.shutdown();
                    continue;
                }

            }

        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}
