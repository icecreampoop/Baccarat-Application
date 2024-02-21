package sg.edu.nus.iss.baccarat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class BaccaratEngine {
    HashMap<String, Float> deckOfCards = new HashMap<>();
    StringBuilder tempDB;

    public BaccaratEngine(int numberOfDecks) {
        for (int x = 0; x < numberOfDecks; x++) {
            int deckCount = 1;

            for (int y = 1; y <= 13; y++) {

                for (int z = 1; z <= 4; z++) {
                    deckOfCards.put("deck" + deckCount + ":" + y + "." + z, Float.parseFloat(y + "." + z));
                }
            }
            deckCount++;
        }

        try {
            Path path = Paths.get("src\\sg\\edu\\n" + //
                                "us\\iss\\baccarat\\server\\cards.db");
            FileReader fileReader = new FileReader(path.toFile());
            FileWriter fileWriter = new FileWriter(path.toFile());
            String lines;
 
            try (BufferedReader dbReader = new BufferedReader(fileReader)) {
                while ((lines = dbReader.readLine()) != null) {
                    tempDB.append(lines).append(System.lineSeparator());
                }
            }

            try (BufferedWriter dbWriter = new BufferedWriter(fileWriter)) {
                for (String x : deckOfCards.keySet()) {
                    dbWriter.write(deckOfCards.get(x).toString());
                    dbWriter.newLine();
                }
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
