package sg.edu.nus.iss.baccarat.server;

import java.util.ArrayList;
import java.util.Collections;

public class BaccaratEngine {
    ArrayList<String> deckOfCards = new ArrayList<>();
    StringBuilder tempDB;
    int numberOfDecks;

    public BaccaratEngine(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
        topUpCardsToDeck();
    }

    public void clearAllCardsInDeck() {
        ServerFileIOHandler.writeToDB(new ArrayList<>());
    }

    public void topUpCardsToDeck() {
        for (int x = 0; x < numberOfDecks; x++) {
            for (int y = 1; y <= 13; y++) {
                for (int z = 1; z <= 4; z++) {
                    deckOfCards.add(String.format("%d.%d",y,z));
                }
            }
        }
        Collections.shuffle(deckOfCards);
        ServerFileIOHandler.writeToDB(deckOfCards);
    }
}
