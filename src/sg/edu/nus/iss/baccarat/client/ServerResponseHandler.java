package sg.edu.nus.iss.baccarat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerResponseHandler implements Runnable {
    private Socket socket;
    private static String serverResponse;
    private static BufferedReader bufferedReader;

    public ServerResponseHandler(Socket socket) {
        this.socket = socket;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public synchronized void serverResponse() {
        while (!socket.isClosed()) {
            try {
                serverResponse = bufferedReader.readLine();

                if (!serverResponse.equals("")) {
                    System.out.println(serverResponse);
                }

            } catch (IOException io) {
                //ignore as this just mean user quit
            }
        }
    }

    @Override
    public void run() {
        serverResponse();
    }

}
