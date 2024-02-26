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
        String serverReply;
        boolean userHasQuit = false;
        boolean serverReplyReady = false;

        try (Socket socket = new Socket(address, port)) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.print("Please log in to your account: ");

            while (!userHasQuit) {
                serverReplyReady = false;
                userInput = scanner.nextLine().toLowerCase().trim();
                
                if (userInput.equals("")) {
                    System.out.println("Please enter a valid input!");
                    continue;
                }

                printWriter.println(userInput);

                // quit program
                if (userInput.equals("quit")) {
                    userHasQuit = true;

                    // closing resources
                    scanner.close();
                    bufferedReader.close();
                    printWriter.close();
                    continue;
                }

                // this shit took me so long, bufferedreader wasnt ready yet and when used on
                // sockets the eof is the socket stream end bruh
                // you read a line on every iteration and leve the loop if readLine returns
                // null.
                // readLine returns only null, if eof is reached (= socked is closed) and
                // returns a String if a '\n' is read.
                while (!serverReplyReady) {
                    if (bufferedReader.ready()) {
                        serverReply = bufferedReader.readLine();

                        if (serverReply.equals("")) {
                            try {
                                //forgive me for my weakness
                                System.out.println("~~~~~Server Loading~~~~~");
                                Thread.sleep(500);
                                continue;
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }

                        System.out.println(serverReply);
                        serverReplyReady = true;
                    }
                }
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}
