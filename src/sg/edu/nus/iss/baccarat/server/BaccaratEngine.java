package sg.edu.nus.iss.baccarat.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class BaccaratEngine {
    static ArrayList<String> deckOfCards = new ArrayList<>();
    StringBuilder tempDB;
    static int numberOfDecks;

    public BaccaratEngine(int numberOfDecks) {
        BaccaratEngine.numberOfDecks = numberOfDecks;
        topUpCardsToDeck();
    }

    public static synchronized void clearAllCardsInDeck() {
        ServerFileIOHandler.writeToDB(new ArrayList<>());
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

    public static synchronized void userLogIn(String userName) {
        if (!Files.exists(Path.of(userName))) {
            try {
                Files.createFile(Path.of("src\\sg\\edu\\nus\\iss\\baccarat\\server", userName + ".db"));
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

    }
}
