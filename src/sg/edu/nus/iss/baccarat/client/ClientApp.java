package sg.edu.nus.iss.baccarat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        String[] stringSplit = args[0].split(":");
        String address = stringSplit[0];
        Integer port = Integer.parseInt(stringSplit[1]);
        Scanner scanner = new Scanner(System.in);
        String userInput;
        int serverInputLength;

        try (Socket socket = new Socket(address, port)) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.print("Please log in to your account: ");

            // this structure of code is more efficient compared to constantly creating a new socket HOWEVER this looks kinda noobish
            while (!(userInput = scanner.nextLine().toLowerCase().trim()).equals("quit")) {
                printWriter.println(userInput);
                serverInputLength = Integer.parseInt(bufferedReader.readLine());
                for (int x = 0; x < serverInputLength; x++) {
                    System.out.println(bufferedReader.readLine());
                }
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

        // closing resources
        scanner.close();
    }
}
