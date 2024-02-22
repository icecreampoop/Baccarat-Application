package sg.edu.nus.iss.baccarat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ServerFileIOHandler {
    static final Path path = Paths.get("src\\sg\\edu\\nus\\iss\\baccarat\\server\\cards.db");
    static StringBuilder tempDB;

    public static synchronized String readFromDB() {
        try {
            FileReader fileReader = new FileReader(path.toFile());
            String lines;

            try (BufferedReader dbReader = new BufferedReader(fileReader)) {
                while ((lines = dbReader.readLine()) != null) {
                    tempDB.append(lines).append(System.lineSeparator());
                }
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
        return tempDB.toString();
    }

    public static synchronized void writeToDB(ArrayList<String> dbArrayList) {
        try {
            FileWriter fileWriter = new FileWriter(path.toFile());

            try (BufferedWriter dbWriter = new BufferedWriter(fileWriter)) {
                for (String x : dbArrayList) {
                    dbWriter.write(x);
                    dbWriter.newLine();
                }
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
