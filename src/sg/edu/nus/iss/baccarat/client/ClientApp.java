package  sg.edu.nus.iss.baccarat.client;

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
        
        try (Socket socket = new Socket(address, port)) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.print("Please log in to your account: ");
            userInput = scanner.nextLine().toLowerCase().trim();
            //while (!(userInput = scanner.nextLine().toLowerCase().trim()).equals("quit")) {
                printWriter.println(userInput);
                System.out.println(bufferedReader.readLine());
            //}

        } catch (IOException io) {
            io.printStackTrace();
        }


        //closing resources
        scanner.close();
    }
}
