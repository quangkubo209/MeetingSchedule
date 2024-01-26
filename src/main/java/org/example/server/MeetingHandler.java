package org.example.server;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.protocol.ProtocolHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MeetingHandler extends Thread {
    private final Socket clientSocket;
    private ProtocolHandler protocolHandler;

    public MeetingHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request = reader.readLine();
            String response = protocolHandler.processRequest(request);
            writer.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
