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
    static StringBuilder tempDB = new StringBuilder();

    public static synchronized String readFromDB() {
        String lines;
        try {
            FileReader fileReader = new FileReader(path.toFile());

            try (BufferedReader dbReader = new BufferedReader(fileReader)) {
                while ((lines = dbReader.readLine()) != null) {
                    tempDB.append(lines).append(" ");
                }
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

        lines = tempDB.toString();
        tempDB.setLength(0);
        return lines;
    }

    public static synchronized String readFromUserAccount(String userName) {
        String lines;
        try {
            FileReader fileReader = new FileReader(Paths.get("src\\sg\\edu\\nus\\iss\\baccarat\\server\\" + userName + ".db").toFile());
            try (BufferedReader userReader = new BufferedReader(fileReader)) {
                while ((lines = userReader.readLine()) != null) {
                    tempDB.append(lines);
                }
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
        lines = tempDB.toString();
        tempDB.setLength(0);
        return lines;
    }

    public static synchronized void writeToUserAccount(String userName, Long accountValue) {
        try {
            FileWriter fileWriter = new FileWriter(Paths.get("src\\sg\\edu\\nus\\iss\\baccarat\\server\\" + userName + ".db").toFile());

            try (BufferedWriter userWriter = new BufferedWriter(fileWriter)) {
                userWriter.write(Long.toString(accountValue));
                userWriter.flush();
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
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
