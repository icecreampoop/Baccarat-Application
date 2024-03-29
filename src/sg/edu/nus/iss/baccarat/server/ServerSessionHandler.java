package sg.edu.nus.iss.baccarat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSessionHandler implements Runnable {
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    Socket clientSocket;
    String userInput;
    String userName = "";
    String whoWonOrDraw = "";
    String[] inputStrings;
    boolean userHasLoggedIn = false;
    boolean userHasLoggedOff = false;
    boolean userHasToPlaceBet = false;
    Long[] tempMoneyHolder = new Long[2]; // index 0 being the current bet size, index 1 being the previous user value
                                          // prior to bet
    ArrayList<ArrayList<String>> dealtCardsStore = new ArrayList<>(); // index 0 for player, index 1 for banker

    public ServerSessionHandler(Socket clientSocket) {
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

        try {
            while (!userHasLoggedOff) {

                if (bufferedReader.ready()) {
                    userInput = bufferedReader.readLine().trim().toLowerCase();

                    if (userInput.equals("")) {
                        continue;
                    }

                } else {
                    continue;
                }

                inputStrings = userInput.split(" ");

                // user quit
                if (inputStrings[0].equals("quit")) {
                    userHasLoggedOff = true;
                    bufferedReader.close();
                    printWriter.close();
                    continue;
                }

                // user log in
                if (inputStrings[0].equals("login")) {

                    // check if user has already logged in
                    if (userHasLoggedIn) {
                        printWriter.println(String.format("> You have already logged in as %s", userName));
                        continue;
                    }

                    // check if user is topping up
                    logInFunction(inputStrings);
                    continue;
                }

                // logic check if user has logged in before attempting other operations
                if (!userHasLoggedIn) {
                    printWriter.println("> Please log in first!");
                    continue;
                }

                // user place bet
                if (inputStrings[0].equals("bet")) {
                    try {
                        tempMoneyHolder[0] = Long.parseLong(inputStrings[1]);

                        if (BaccaratEngine.userhasEnoughMoney(tempMoneyHolder[0], userName)) {
                            tempMoneyHolder[1] = Long.parseLong(ServerFileIOHandler.readFromUserAccount(userName));
                            userHasToPlaceBet = true;
                            printWriter.println("> Choose a side to bet on, deal p for player or deal b for banker");
                        } else {
                            printWriter.println("> You do not have enough money for this");
                        }
                        continue;
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException error) {
                        // ignore
                    }

                }

                // after placing bet amount user HAS to indicate which side (player or banker)
                if (userHasToPlaceBet) {
                    if (inputStrings[0].equals("deal")) {
                        try {
                            if (inputStrings[1].equals("b") || inputStrings[1].equals("p")) {
                                dealtCardsStore = BaccaratEngine.dealCardsForPlay();

                                printWriter.println("------------------------------------");
                                printWriter.println(BaccaratEngine.formatDealtCards(dealtCardsStore));
                                printWriter.println("------------------------------------");

                                whoWonOrDraw = BaccaratEngine.whoWinsOrDraw(dealtCardsStore);

                                if (inputStrings[1].equals(whoWonOrDraw)) {
                                    playerWinsBet();
                                    userHasToPlaceBet = false;
                                    continue;

                                } else if (whoWonOrDraw.equals("draw")) {
                                    printWriter.println(
                                            String.format("> It was a DRAW! Your current account value stays at $%s.",
                                                    BaccaratEngine.userLogIn(userName)));
                                    userHasToPlaceBet = false;
                                    continue;
                                } else {
                                    playerLosesBet();
                                    userHasToPlaceBet = false;
                                    continue;
                                }

                            }
                        } catch (ArrayIndexOutOfBoundsException out) {
                            // ignore
                        }
                    }

                }

                printWriter.println("> Please enter a valid input!");
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

        System.out.println(String.format("> %s has disconnected from Baccarat Server %s", userName,
                Thread.currentThread().getName()));

    }

    private void playerWinsBet() {
        printWriter.println(String.format("> YOU WON $%s! Your current account value is $%s.", tempMoneyHolder[0],
                BaccaratEngine.userLogIn(userName, tempMoneyHolder[1] + tempMoneyHolder[0])));
    }

    private void playerLosesBet() {
        printWriter.println(String.format("> You lost $%s! Your current account value is $%s.", tempMoneyHolder[0],
                BaccaratEngine.userLogIn(userName, tempMoneyHolder[1] - tempMoneyHolder[0])));
    }

    private void logInFunction(String[] userStrings) {
        if (userStrings.length == 3) {

            // sanitize the string for my sanity
            userStrings[2] = userStrings[2].replace(",", "").replace("$", "");

            if (BaccaratEngine.numberRealisticCheck(userStrings[2])) {
                userName = userStrings[1];
                userHasLoggedIn = true;

                printWriter.println(String.format("> Welcome %s, your current account value is : $%s", userName,
                        BaccaratEngine.userLogIn(userName, Long.parseLong(userStrings[2]))));
                
                System.out.println(String.format("> %s has logged onto Baccarat Server %s", userName,
                        Thread.currentThread().getName()));
            } else {
                printWriter.println(
                        "> Please enter a REALISTIC NUMBER (any value inbetween 999,999,999,999,999,999 ~ -999,999,999,999,999,999)");
            }

            // login without topping up
        } else if (userStrings.length == 2) {
            userName = userStrings[1];
            userHasLoggedIn = true;

            printWriter.println(String.format("> Welcome %s, your current account value is : $%s", userName,
                    BaccaratEngine.userLogIn(userName)));

            System.out.println(String.format("> %s has logged onto Baccarat Server %s", userName,
                    Thread.currentThread().getName()));
        }

    }
}
