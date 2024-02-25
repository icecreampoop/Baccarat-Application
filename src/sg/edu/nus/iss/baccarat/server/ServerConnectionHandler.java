package sg.edu.nus.iss.baccarat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnectionHandler implements Runnable {
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    Socket clientSocket;
    String userInput;
    String userName;
    String[] inputStrings;

    public ServerConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException io) {
            io.printStackTrace();
        }

        while (!clientSocket.isClosed()){
            try {
                userInput = bufferedReader.readLine().trim().toLowerCase();
                inputStrings = userInput.split(" ");

                //user log in
                if (inputStrings[0].equals("login")) {
                    userName = inputStrings[1];
                    printWriter.println(2);
                    printWriter.printf("Welcome %s!\nYour current account value is : $%s\n", userName, BaccaratEngine.userLogIn(userName, Integer.parseInt(inputStrings[2])));
                }

            } catch (IOException io) {
                io.printStackTrace();
                printWriter.println("Please key in a valid input!");
            }
        }

        System.out.printf("%s has disconnected from the server.", userName);

    }

}
