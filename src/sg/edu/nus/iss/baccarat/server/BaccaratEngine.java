package sg.edu.nus.iss.baccarat.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class BaccaratEngine {
    static private ArrayList<String> deckOfCards = new ArrayList<>();
    static private int numberOfDecks;
    static private String[] suitCheckStore = new String[2];
    static private ArrayList<ArrayList<String>> dealtCardsStore = new ArrayList<>(); // index 0 for player, index 1 for banker
    static private StringBuffer cardsFormatToStringBuffer = new StringBuffer();

    public BaccaratEngine(int numberOfDecks) {
        BaccaratEngine.numberOfDecks = numberOfDecks;
        topUpCardsToDeck();
    }

    public static synchronized void topUpCardsToDeck() {
        for (int x = 0; x < numberOfDecks; x++) {
            for (int y = 1; y <= 13; y++) {
                for (int z = 1; z <= 4; z++) {
                    deckOfCards.add(String.format("%d.%d", y, z));
                }
            }
        }
        Collections.shuffle(deckOfCards);
        ServerFileIOHandler.writeToDB(deckOfCards);
    }

    public static synchronized String userLogIn(String userName) {
        if (!Files.exists(Path.of("src\\sg\\edu\\nus\\iss\\baccarat\\server\\" + userName + ".db"))) {
            try {
                Files.createFile(Path.of("src\\sg\\edu\\nus\\iss\\baccarat\\server", userName + ".db"));
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        return ServerFileIOHandler.readFromUserAccount(userName);
    }

    public static synchronized String userLogIn(String userName, Long topUpAccountValue) {
        if (!Files.exists(Path.of("src\\sg\\edu\\nus\\iss\\baccarat\\server\\" + userName + ".db"))) {
            try {
                Files.createFile(Path.of("src\\sg\\edu\\nus\\iss\\baccarat\\server", userName + ".db"));
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        ServerFileIOHandler.writeToUserAccount(userName, topUpAccountValue);
        return ServerFileIOHandler.readFromUserAccount(userName);
    }

    private static synchronized String valueOfHand(ArrayList<String> handArrayList) {
        //TODO use streams, also btw not so easy cus need to calc properly so logic works both on 2/3 card draws
        return "6";//handArrayList.stream().filter(x -> 10 > );
    }

    public static synchronized boolean userhasEnoughMoney(Long betSize, String userName) {
        return Long.parseLong(ServerFileIOHandler.readFromUserAccount(userName)) >= betSize;
    }

    public static synchronized ArrayList<ArrayList<String>> dealCardsForPlay() {
        deckOfCards.clear();
        dealtCardsStore.clear();
        dealtCardsStore.add(new ArrayList<>());
        dealtCardsStore.add(new ArrayList<>());

        for (String cards : ServerFileIOHandler.readFromDB().split(" ")) {
            deckOfCards.add(cards);
        }

        for (int x = 0; x < 4; x++) {
            if (x % 2 == 0) {
                //player hand
                dealtCardsStore.get(0).add(deckOfCards.get(0));
            } else {
                //banker hand
                dealtCardsStore.get(1).add(deckOfCards.get(0));
            }
            deckOfCards.remove(0);
        }

        //check if player needs to draw
        if (5 > Byte.parseByte(valueOfHand(dealtCardsStore.get(0)))) {
            dealtCardsStore.get(0).add(deckOfCards.get(0));
            deckOfCards.remove(0);
        }
        //check if banker needs to draw
        if (5 > Byte.parseByte(valueOfHand(dealtCardsStore.get(1)))) {
            dealtCardsStore.get(1).add(deckOfCards.get(0));
            deckOfCards.remove(0);
        }

        dealtCardsStore.get(0).add(valueOfHand(dealtCardsStore.get(0)));
        dealtCardsStore.get(1).add(valueOfHand(dealtCardsStore.get(1)));

        ServerFileIOHandler.writeToDB(deckOfCards);
        return dealtCardsStore;
    }

    public static synchronized String whoWinsOrDraw(ArrayList<ArrayList<String>> inputList) {

        if (Byte.parseByte(inputList.get(0).get(inputList.get(0).size() - 1)) > Byte
                .parseByte(inputList.get(1).get(inputList.get(1).size() - 1))) {
            return "p";
        } else if (Byte.parseByte(inputList.get(0).get(inputList.get(0).size() - 1)) < Byte
                .parseByte(inputList.get(1).get(inputList.get(1).size() - 1))) {
            return "b";
        } else {
            return "draw";
        }
    }

    public static synchronized String formatDealtCards(ArrayList<ArrayList<String>> formatStore) {
        cardsFormatToStringBuffer.setLength(0);
        
        cardsFormatToStringBuffer.append("Player | ");
        for (int x = 0; x < formatStore.get(0).size() - 1; x++) {
            suitCheckStore = formatStore.get(0).get(x).split("\\.");
            
            switch (suitCheckStore[1]) {
                case "1":
                    cardsFormatToStringBuffer.append(suitCheckStore[0]).append(" HEARTS ");
                    break;
                case "2":
                    cardsFormatToStringBuffer.append(suitCheckStore[0]).append(" DIAMONDS ");
                    break;
                case "3":
                    cardsFormatToStringBuffer.append(suitCheckStore[0]).append(" SPADES ");
                    break;
                case "4":
                    cardsFormatToStringBuffer.append(suitCheckStore[0]).append(" CLUBS ");
            }
        }
        
        cardsFormatToStringBuffer.append("\n");
        
        cardsFormatToStringBuffer.append("Banker | ");
        for (int y = 0; y < formatStore.get(1).size() - 1; y++) {
            suitCheckStore = formatStore.get(1).get(y).split("\\.");

            switch (suitCheckStore[1]) {
                case "1":
                    cardsFormatToStringBuffer.append(suitCheckStore[0]).append(" HEARTS ");
                    break;
                case "2":
                    cardsFormatToStringBuffer.append(suitCheckStore[0]).append(" DIAMONDS ");
                    break;
                case "3":
                    cardsFormatToStringBuffer.append(suitCheckStore[0]).append(" SPADES ");
                    break;
                case "4":
                    cardsFormatToStringBuffer.append(suitCheckStore[0]).append(" CLUBS ");
            }
        }

        cardsFormatToStringBuffer.append("\n");

        switch (whoWinsOrDraw(formatStore)) {
            case "p":
                cardsFormatToStringBuffer.append("Player Hand Wins");
                break;
            case "b":
                cardsFormatToStringBuffer.append("Banker Hand Wins");
                break;
        }

        return cardsFormatToStringBuffer.toString();
    }

    public static synchronized boolean numberRealisticCheck(String userNumber) {
        return String.valueOf(Long.MAX_VALUE).length() >= (userNumber.length() + 1);
    }
}
